/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.importdataformcsv.impl;

import com.huawei.FortifyUtiltools.FortifyUtils;


import com.huawei.utils.CheckUtils;
import com.huawei.utils.CollectionUtil;
import com.huawei.utils.CommonUtil;
import com.huawei.utils.StringUtil;
import com.huawei.vi.entity.po.FiledInfo;
import com.huawei.vi.thirddata.mapper.TblOriginalDataMapper;
import com.huawei.vi.thirddata.mapper.TblPeriodManageMapper;
import com.huawei.vi.thirddata.service.binary.pojo.TblPeriodManage;
import com.huawei.vi.thirddata.service.collection.CollectionService;
import com.huawei.vi.thirddata.service.dzserver.DzCollectService;
import com.huawei.vi.thirddata.service.importdataformcsv.IoriginalDataService;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 原始数据业务层实现类
 *
 * @version 1.0, 2018年7月30日
 * @since 2018-07-30
 */
@Service(value = "originalDataService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class OriginalDataServiceImpl implements IoriginalDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OriginalDataServiceImpl.class);

    private static final String CHANGE = "change";

    private static final String DROP_COLUMN = "drop column";

    private static final String TCP = "TCP";

    private static final String UDP = "UDP";

    private static final int NIGHTEEN = 19;

    private static final int UNIT = 1024;

    private static final int NUM_0 = NumConstant.NUM_0;

    private static final String TAB_NAME = "tabName";

    private static final String PATH = "path";

    private static final String FILED = "filed";

    File fileAnalyse = FileUtils.getFile(CommonConst.ANALYSEPATH); // 需要分析的文件路径

    String analyse = CommonConst.ANALYSEPATH;

    @Autowired
    private DzCollectService dzCollectService;

    @Autowired
    private CollectionService collectionService;

    @Resource(name = "originalDataDao")
    private TblOriginalDataMapper originalDataDao;

    @Resource(name = "periodManageDao")
    private TblPeriodManageMapper periodManageDao;

    @Value("${configBean.inputPath:''}")
    private String inputPath;

    /**
     * 是否用于功能演示 主要影响探针离线&东芝在线功能
     */
    @Value("${configBean.isShow:true}")
    private boolean isShow;

    @Value("${configBean.allFieldNum4Tcp:24}")
    private Integer allFieldNum4Tcp;

    @Value("${configBean.allFieldNum4Udp:19}")
    private Integer allFieldNum4Udp;

    /**
     * start
     *
     * @param allPathlists allPathlists
     * @param tcpPathlists tcpPathlists
     * @param udpPathlists udpPathlists
     * @param periodStartDate periodStartDate
     * @return Map
     */
    @Override
    public Map<String, Object> start(List<String> allPathlists, List<String> tcpPathlists, List<String> udpPathlists,
        Date periodStartDate) {
        Map<String, Object> resultMap = new HashMap<>();
        if (CommonUtil.isExistsNull(allPathlists, tcpPathlists, udpPathlists, periodStartDate)) {
            resultMap.put(CommonConstant.FLAG, false);
            return resultMap;
        }
        Map<String, Boolean> fileFlags = new HashMap<String, Boolean>();
        String tcpTabName = CollectionUtils.isNotEmpty(tcpPathlists)
            ? UtilMethod.getTableName(CommonConst.TABLE_ORIGINAL_DATA, periodStartDate) : null;
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put(TAB_NAME, tcpTabName);

        // 原始数据入库及必选字段规整开始
        LOGGER.info("original data into DB start ");
        int index = 0;
        for (String filePath : tcpPathlists) {
            if (StringUtils.isNotBlank(filePath)) {
                conditions.put(PATH, filePath);

                // 解析源文件入库
                boolean eachOriginalFlag = isOriginalDataIntoMysql(conditions);
                if (index < allPathlists.size()) {
                    fileFlags.put(allPathlists.get(index), eachOriginalFlag);
                    if (!eachOriginalFlag) {
                        LOGGER.error("load tcp original data is error..");
                    }
                }
            } else {
                LOGGER.error("get filePath is empty when insert original data");
            }
            index++;
        }
        String udpTabName = CollectionUtils.isNotEmpty(udpPathlists)
            ? UtilMethod.getTableName(CommonConst.TABLE_ORIGINAL_DATA_UDP, periodStartDate) : null;
        conditions.put(TAB_NAME, udpTabName);
        for (String filePath : udpPathlists) {
            if (StringUtils.isNotBlank(filePath)) {
                conditions.put(PATH, filePath);

                // 解析源文件入库
                boolean eachOriginalFlag = isOriginalDataIntoMysql(conditions);
                if (index < allPathlists.size()) {
                    fileFlags.put(allPathlists.get(index), eachOriginalFlag);
                    if (!eachOriginalFlag) {
                        LOGGER.error("load udp original data is error..");
                    }
                }
            } else {
                LOGGER.info("get filePath is empty when insert original data");
            }
            index++;
        }
        return constructMap(fileFlags, tcpTabName, udpTabName);
    }

    /**
     * start
     */
    @Override
    public void start() {
        // 文件排序获取需要入库的文件路径
        Map<String, List<String>> pathLists = sort();
        List<String> allFilePaths = pathLists.get("allFilePath"); // 获取原始文件列表
        List<String> tcpPathLists = pathLists.get("tcpPathlist"); // 获取tcp文件列表
        List<String> udpPathLists = pathLists.get("udpPathlist"); // 获取udp文件列表
        getNewestPath(tcpPathLists);
        getNewestPath(udpPathLists);
        if (CollectionUtils.isNotEmpty(tcpPathLists) || CollectionUtils.isNotEmpty(udpPathLists)) {
            // 得到周期时间
            String tcpPath = CollectionUtils.isNotEmpty(tcpPathLists) ? tcpPathLists.get(0) : null;
            String udpPath = CollectionUtils.isNotEmpty(udpPathLists) ? udpPathLists.get(0) : null;
            String periodStartTime = UtilMethod.getPeriodStartDate(!StringUtil.isNull(tcpPath) ? tcpPath : udpPath);
            Date periodStartDate = UtilMethod.stringPeriodToDate(periodStartTime);

            // 动态创建tbl_original_data表(tcp数据表)
            createOriginal(tcpPath, periodStartDate, CommonConst.TABLE_ORIGINAL_DATA);

            // 动态创建tbl_original_data_udp表(UDP数据表)
            createOriginal(udpPath, periodStartDate, CommonConst.TABLE_ORIGINAL_DATA_UDP);
            Map<String, Object> maps = start(allFilePaths, tcpPathLists, udpPathLists, periodStartDate);

            // 得到最新的组网关系表名，并将周期时间和组网关系表对应关系存到关系表中
            if ((boolean) maps.get(CommonConstant.FLAG)) {
                // 如库周期管理表
                insertPeriodManage(periodStartDate);
                List<FiledInfo> tcpList = BaseConfig.getFiledInfoList();
                List<FiledInfo> udpList = BaseConfig.getUdpFiledInfoList();
                FiledClass filedClass = new FiledClass(allFieldNum4Tcp, allFieldNum4Udp);
                operateTcpOrUdp(maps.get("tcpTabName"), filedClass, tcpList, TCP);
                operateTcpOrUdp(maps.get("udpTabName"), filedClass, udpList, UDP);
                if (isShow) {
                    dzCollectService.dzServerCollectStart(collectionService.getServerParamInfo(), periodStartDate);
                }
            }
        }
    }

    private Map<String, Object> constructMap(Map<String, Boolean> fileFlags, String tcpTabName, String udpTabName) {
        // 原始数据入库及必选字段规整结束
        Map<String, Object> resultMap = new HashMap<String, Object>();
        LOGGER.info("original data into DB end ");
        resultMap.put("fileInputFlag", fileFlags);
        resultMap.put(CommonConstant.FLAG, true);
        resultMap.put("isAllOk", true);

        // 返回表名称，用于在后面接收，做表的alter处理
        resultMap.put("tcpTabName", tcpTabName);
        resultMap.put("udpTabName", udpTabName);
        return resultMap;
    }

    /**
     * 原始数据入库mySql
     *
     * @param condition 入参条件
     * @return isOriginalDataIntoMysql [返回类型说明]
     */
    public boolean isOriginalDataIntoMysql(Map<String, Object> condition) {
        if (CommonUtil.isNotNull(FortifyUtils.getSysProperty(CommonConstant.OS_NAME))) {
            String systemName = FortifyUtils.getSysProperty(CommonConstant.OS_NAME).toLowerCase(Locale.ENGLISH);
            if (systemName.startsWith(CommonConstant.OS_LINUX)) {
                return originalDataDaoInsertOriginalDataInLinux(condition);
            } else {
                return originalDataDaoInsertOriginalData(condition);
            }
        }
        return false;
    }

    private boolean originalDataDaoInsertOriginalDataInLinux(Map<String, Object> condition) {
        try {
            return originalDataDao.insertOriginalDataInLinux(condition) > NUM_0;
        } catch (DataAccessException e) {
            LOGGER.error("insertOriginalDataInLinux DataAccessException {} ", e.getMessage());
        }
        return false;
    }

    private boolean originalDataDaoInsertOriginalData(Map<String, Object> condition) {
        try {
            return originalDataDao.insertOriginalData(condition) > NUM_0;
        } catch (DataAccessException e) {
            LOGGER.error("insertOriginalData DataAccessException {} ", e.getMessage());
        }
        return false;
    }

    /**
     * 多文件
     *
     * @return Map 返回值
     */
    @Override
    public Map<String, List<String>> sort() {
        // 用于存放不同格式的文件
        Map<String, List<String>> maps = new HashMap<String, List<String>>();
        if (StringUtil.isEmpty(inputPath)) {
            LOGGER.error("configBean inputPath is empty!");
            return maps;
        }

        // 存放两种类型的文件
        List<String> udpPathlists = new ArrayList<String>();
        List<String> tcpPathlists = new ArrayList<String>();

        // 获取input目录下存放每个文件夹的绝对路径
        List<String> allDirOfInputs = getPathOnInputPath(inputPath);
        if (CollectionUtil.isEmpty(allDirOfInputs)) {
            LOGGER.error("input path files list is empty");
            return maps;
        }
        List<String> tcpFilelists = new ArrayList<String>();
        List<String> udpFilelists = new ArrayList<String>();
        List<String> allList = new ArrayList<String>();
        foreachInput4TcpUdp(allDirOfInputs, tcpFilelists, udpFilelists, allList);
        maps.put("allFilePath", allList);

        // 清空存放需要分析的文件的文件夹
        // 用于保存udp格式的文件
        List<File> udpList = new ArrayList<File>();

        // 用于保存tcp格式的文件
        List<File> tcpList = new ArrayList<File>();
        fileLog(delAllFile(analyse), analyse);
        for (int ii = 0; ii < allDirOfInputs.size(); ii++) {
            clearFileList(udpList, tcpList);

            // 需要在此处制空udplist和tcplist
            clear(tcpFilelists, udpFilelists);

            File directoryName = FileUtils.getFile(allDirOfInputs.get(ii));
            if (!directoryName.isDirectory()) {
                continue;
            }

            // analyseOverPath:WebContent/tmp/analyseOver
            String basePath = getPath(CommonConst.ANALYSEOVERPATH, File.separator, directoryName.getName());
            File pathName = getFile(basePath);

            // 对Linux系统下的pathName及子文件 进行赋权限操作 (因为这些文件是后来代码根据需要自动创建的)
            permission4Linux(pathName);
            File[] files = directoryName.listFiles();
            if (files == null || 0 == files.length) {
                continue;
            }

            // 遍历files数组，取出udp文件和tcp文件
            tcpOrUdpFile(files, tcpList, udpList);
            getLastTcpOrUdp(tcpList, udpList, tcpFilelists, udpFilelists);
            File pathNameAnalyseOver = getFile(getPath(basePath, File.separator, "AnalyseOver"));
            File pathNameAnalyseChOver = getFile(getPath(basePath, File.separator, "AnalyseCHOver"));
            String inputFilePath = allDirOfInputs.get(ii);

            // 复制tcp文件到分析文件夹，同时复制到备份文件夹下，然后删除源文件
            copyAndDelFile(tcpFilelists, pathNameAnalyseOver, directoryName);

            // 复制udp文件到分析文件夹，同时复制到备份文件夹下，然后删除源文件
            copyAndDelFile(udpFilelists, pathNameAnalyseOver, directoryName);
            copyLog(ioCopy(inputFilePath, getCanonicalPath(pathNameAnalyseChOver)), inputFilePath);

            // 复制完成后删除当前文件夹里面内容
            fileLog(delAllFile(allDirOfInputs.get(ii)), allDirOfInputs.get(ii));
            tcpOrUdpNewName(tcpFilelists, udpFilelists, tcpPathlists, udpPathlists);
        }
        maps.put("tcpPathlist", tcpPathlists);
        maps.put("udpPathlist", udpPathlists);
        return maps;
    }

    /**
     * 清空集合
     *
     * @param tcpFilelist 原始tcp
     * @param udpFilelist 原始udp
     */
    private void clear(List<String> tcpFilelist, List<String> udpFilelist) {
        if (CommonUtil.isNotNull(tcpFilelist, udpFilelist)) {
            tcpFilelist.clear();
            udpFilelist.clear();
        }
    }

    private void clearFileList(List<File> tcpList, List<File> udpList) {
        if (CommonUtil.isNotNull(tcpList, udpList)) {
            tcpList.clear();
            udpList.clear();
        }
    }

    /**
     * 遍历input路径下，区分汇总 tcp udp
     *
     * @param allDirOfInput 所以输入文件夹
     * @param tcpFilelist 原始tcp
     * @param udpFilelist 原始udp
     * @param allList allList
     */
    private void foreachInput4TcpUdp(List<String> allDirOfInput, List<String> tcpFilelist, List<String> udpFilelist,
        List<String> allList) {
        List<File> tcpList = new ArrayList<File>(); // 用于保存tcp格式的文件
        List<File> udpList = new ArrayList<File>(); // 用于保存udp格式的文件
        for (int ii = 0; ii < allDirOfInput.size(); ii++) {
            tcpList.clear();
            udpList.clear();

            // path:input文件夹中的文件的路径
            // files数组中保存的是所有的csv文件
            File[] files = FileUtils.getFile(allDirOfInput.get(ii)).listFiles();
            if (files == null || files.length == 0) {
                continue;
            }

            // 遍历files数组，取出udp文件和tcp文件
            getTcpOrUdp(files, tcpList, udpList);
            getLastTcpOrUdpFile(tcpList, udpList, tcpFilelist, udpFilelist, allList);
        }
    }

    /**
     * 对需要分析的文件进行修改名字
     *
     * @param tcpFilelist 原始tcp
     * @param udpFilelist 原始udp
     * @param tcpPathlists 新tcp
     * @param udpPathlists 新udp
     */
    private void tcpOrUdpNewName(List<String> tcpFilelist, List<String> udpFilelist, List<String> tcpPathlists,
        List<String> udpPathlists) {
        String newTcpName = updateFileName(tcpFilelist);
        String newUdpName = updateFileName(udpFilelist);
        if (!CheckUtils.isNullOrBlank(newTcpName)) {
            tcpPathlists.add(newTcpName);
        }
        if (!CheckUtils.isNullOrBlank(newUdpName)) {
            udpPathlists.add(newUdpName);
        }
    }

    /**
     * 获取文件路径
     *
     * @param file file
     * @return path
     */
    private String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return "";
    }

    /**
     * 赋权
     *
     * @param file file
     */
    private void permission4Linux(File file) {
        try {
            PermissionUtil.permission4Linux(file.getCanonicalFile().getCanonicalPath());
        } catch (IOException e2) {
            LOGGER.error("file getCanonicalPath failed : {}", e2.getMessage());
        }
    }

    /**
     * 当删除文件失败时，打印日志
     *
     * @param isSucces 删除是否成功
     * @param path path
     */
    private void fileLog(boolean isSucces, String path) {
        if (!isSucces) {
            LOGGER.error("delete file fail");
        }
    }

    private void copyLog(boolean isSucces, String path) {
        if (!isSucces) {
            LOGGER.error("copy file fail ");
        }
    }

    /**
     * 获取最新的TCP 或 UDP 文件
     *
     * @param tcpList 新tcp
     * @param udpList 新udp
     * @param tcpFilelist 原始tcp
     * @param udpFilelist 原始udp
     */
    private void getLastTcpOrUdp(List<File> tcpList, List<File> udpList, List<String> tcpFilelist,
        List<String> udpFilelist) {
        File newestTcpFile = getFileByDate(tcpList);
        File newestUdpFile = getFileByDate(udpList);
        if (CommonUtil.isNull(newestTcpFile, newestUdpFile)) {
            LOGGER.error("parameter newestTcpFile newestUdpFile is null!");
            return;
        }
        if (getFileDate(newestTcpFile) > getFileDate(newestUdpFile)) {
            // 存放每个文件夹下边最新的tcp文件
            tcpFilelist.add(newestTcpFile.getName());
        } else if (getFileDate(newestTcpFile) < getFileDate(newestUdpFile)) {
            // 存放每个文件夹下边最新的udp文件
            udpFilelist.add(newestUdpFile.getName());
        } else {
            tcpFilelist.add(newestTcpFile.getName());
            udpFilelist.add(newestUdpFile.getName());
        }
    }

    private void tcpOrUdpFile(File[] files, List<File> tcpList, List<File> udpList) {
        for (File oneFile : files) {
            try (InputStream fr = Files.newInputStream(Paths.get(oneFile.getCanonicalPath()));
                InputStreamReader ir = new InputStreamReader(fr, "GBK"); BufferedReader br = new BufferedReader(ir);
                LineIterator lineIterator = new LineIterator(br)) {
                String oneLine = ""; // 用于保存读取到的一行数据
                if (lineIterator.hasNext()) {
                    oneLine = lineIterator.next();
                    if (!StringUtil.isNull(oneLine)) {
                        if (filterOriginalDataType(oneLine)) {
                            // 属于udp文件
                            udpList.add(oneFile);
                        } else {
                            // 属于tcp文件
                            tcpList.add(oneFile);
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e.getMessage());
            } catch (FileNotFoundException e) {
                LOGGER.error("FileNotFoundException TCP Or UDP CSV ");
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private File getFile(String path) {
        File file = FileUtils.getFile(path);
        if (!file.exists()) {
            boolean isMkdirsSuccess = file.mkdirs();
            if (!isMkdirsSuccess) {
                LOGGER.error("mkdirs file fail path:{} ", path);
            }
        }
        return file;
    }

    /**
     * 拼接file 文件路径
     *
     * @param path 文件路径
     * @return sb 拼接后文件路径
     */
    private String getPath(String... path) {
        StringBuilder stringBuilder = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        Stream.of(path).forEach(str -> stringBuilder.append(str));
        return stringBuilder.toString();
    }

    /**
     * 获取最新的tcp或udp文件
     *
     * @param tcpList tcpList
     * @param udpList udpList
     * @param tcpFilelist tcp Filelist
     * @param udpFilelist udp Filelist
     * @param allList allList
     */
    private void getLastTcpOrUdpFile(List<File> tcpList, List<File> udpList, List<String> tcpFilelist,
        List<String> udpFilelist, List<String> allList) {
        // 最新的TCP文件
        File newestTcpFile = getFileByDate(tcpList);

        // 最新的UDP文件
        File newestUdpFile = getFileByDate(udpList);
        try {
            if (getFileDate(newestTcpFile) > getFileDate(newestUdpFile)) {
                // 存放每个文件夹下边最新的文件
                tcpFilelist.add(newestTcpFile.getName());
                allList.add(newestTcpFile.getCanonicalFile().getCanonicalPath());
            } else if (getFileDate(newestTcpFile) < getFileDate(newestUdpFile)) {
                // 存放每个文件夹下边最新的文件
                udpFilelist.add(newestUdpFile.getName());
                allList.add(newestUdpFile.getCanonicalFile().getCanonicalPath());
            } else {
                tcpFilelist.add(newestTcpFile.getName());
                udpFilelist.add(newestUdpFile.getName());
                allList.add(newestTcpFile.getCanonicalFile().getCanonicalPath());
                allList.add(newestUdpFile.getCanonicalFile().getCanonicalPath());
            }
        } catch (IOException e) {
            LOGGER.error("file getCanonicalPath fail: {}", e.getMessage());
        }
    }

    /**
     * 遍历files数组，取出udp文件和tcp文件
     *
     * @param files files
     * @param udpList udp
     * @param tcpList tcp
     */
    private void getTcpOrUdp(File[] files, List<File> tcpList, List<File> udpList) {
        for (File oneFile : files) {
            String oneLine = ""; // 用于保存读取到的一行数据
            try (InputStream fis = FortifyUtils.newFileInputStream(oneFile);
                 InputStreamReader isr = new InputStreamReader(fis, "GBK");
                 BufferedReader br = new BufferedReader(isr)) {
                oneLine = FortifyUtils.readLine(br);
                if (oneLine != null) {
                    if (filterOriginalDataType(oneLine)) {
                        // 属于udp文件
                        udpList.add(oneFile);
                    } else {
                        // 属于tcp文件
                        tcpList.add(oneFile);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e.getMessage());
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    /**
     * 获取inputpath 路径下的所有文件路径
     *
     * @param path path
     * @return List
     */
    private List<String> getPathOnInputPath(String path) {
        File file = FileUtils.getFile(inputPath); // 源文件路径
        List<String> allDirOfInputs = new ArrayList<String>();
        try {
            // 对Linux系统赋权限
            PermissionUtil.permission4Linux(file.getCanonicalFile().getCanonicalPath());
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (CommonUtil.isNull(file)) {
                    LOGGER.error("files is null ");
                    return allDirOfInputs;
                }
                for (File fileIndex : files) {
                    // fileIndex:input文件夹下边的文件夹
                    if (fileIndex.isDirectory()) {
                        // 获取input目录下存放每个文件夹的绝对路径
                        allDirOfInputs.add(fileIndex.getCanonicalFile().getCanonicalPath());
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("file getCanonicalPath fail: {}", e.getMessage());
        }
        return allDirOfInputs;
    }

    private boolean filterOriginalDataType(String sline) {
        List<String> udpFilterList = BaseConfig.getUdpFilterList();
        for (String udpFilter : udpFilterList) {
            if (!sline.contains(udpFilter)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 通过比较文件名里的时间段获取最新文件
     *
     * @param listFile 所有文件集合
     * @return 最新的文件
     */
    private File getFileByDate(List<File> listFile) {
        int index = 0;
        if (CollectionUtil.isNotEmpty(listFile)) {
            for (int ii = 0; ii < listFile.size(); ii++) {
                if (index < listFile.size()) {
                    long ofileTime = getFileDate(listFile.get(ii));
                    long efileTime = getFileDate(listFile.get(index));
                    if (ofileTime > efileTime) {
                        index = ii;
                    }
                }
            }
            if (index < listFile.size()) {
                return listFile.get(index);
            }
        }
        LOGGER.error("listFile is null");
        return null;
    }

    /**
     * 根据文件名截取时间段，获取文件的操作日期 如：2018-04-21 01.00.30.csv
     *
     * @param file 需要操作的文件
     * @return long 文件操作日期，转为毫秒
     */
    private long getFileDate(File file) {
        if (file != null) {
            long time = 0L;
            String fileTime = getTimeFormFileName(file.getName());
            if (StringUtil.isEmpty(fileTime)) {
                LOGGER.error("fileTime is null or empty");
                return time;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
            try {
                time = sdf.parse(fileTime).getTime();
            } catch (ParseException e) {
                LOGGER.error("get file time exception");
            }
            return time;
        }
        LOGGER.error("file is null");
        return 0;
    }

    /**
     * 采用正则的方式从文件中匹配出时间
     *
     * @param fileName fileName
     * @return datetime
     */
    private String getTimeFormFileName(String fileName) {
        Pattern pattern = Pattern.compile("([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}.[0-9]{2}.[0-9]{2}).csv");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 删除文件夹下面的所有文件
     *
     * @param path path
     * @return boolean
     */
    private static boolean delAllFile(String path) {
        boolean isFlag = false;
        File file = FileUtils.getFile(path);
        if (!file.exists()) {
            return isFlag;
        }
        if (!file.isDirectory()) {
            return isFlag;
        }
        String[] tempLists = file.list();
        File temp = null;
        boolean isNormalize = true;
        if (tempLists != null) {
            for (int ii = 0; ii < tempLists.length; ii++) {
                try {
                    isNormalize = file.getCanonicalPath().endsWith(Normalizer.normalize(File.separator, Form.NFKC));
                } catch (IOException e) {
                    LOGGER.error("file getCanonicalPath failed.");
                }
                if (isNormalize) {
                    temp = FileUtils.getFile(path + tempLists[ii]);
                } else {
                    temp = FileUtils.getFile(path + File.separator + tempLists[ii]);
                }
                if (temp.isFile()) {
                    boolean isRt = temp.delete();
                    if (!isRt) {
                        LOGGER.error("delete file failed.");
                    }
                }
                if (temp.isDirectory()) {
                    delAllFile(path + File.separator + tempLists[ii]); // 先删除文件夹里面的文件
                    isFlag = true;
                }
            }
        } else {
            LOGGER.info("null file.");
        }
        return isFlag;
    }

    /**
     * 对需要分析的文件进行复制同事删除原来的文件
     *
     * @param fielList 需要复制的文件的
     * @param pathNameAnalyseOver 复制文件之后文件的保存路径
     * @param path input文件夹下的直接子文件的路径
     */
    private void copyAndDelFile(List<String> fielList, File pathNameAnalyseOver, File path) {
        if (fielList != null && fielList.size() > 0) {
            for (String fileName : fielList) {
                File fileCopy = FileUtils.getFile(path + File.separator + fileName);
                copy(fileCopy, fileAnalyse);
                LOGGER.info("copy fileCopy to fileAnalyse");
                copy(fileCopy, pathNameAnalyseOver); // 复制最新文件到备份目录
                LOGGER.info("copy fileCopy to pathNameAnalyseOver");
                boolean isFl1 = fileCopy.delete(); // 删除input里面最新的文件
                if (isFl1) {
                    LOGGER.info("Delete the latest input file inside the success");
                } else {
                    LOGGER.info("Delete the latest input file inside the fail");
                }
            }
        }
    }

    /**
     * 对文件进行复制
     *
     * @param f1 文件1
     * @param f2 文件二
     * @throws IOException 异常捕获
     */
    private void copy(File f1, File f2) {
        if (!f2.exists()) {
            boolean isRt = f2.mkdirs();
            if (!isRt) {
                LOGGER.error("mkdir file failed. ");
            }
        }
        if (!f2.exists()) {
            // 路径判断，是路径还是单个的文件
            File[] cfs = f1.listFiles();
            if (cfs == null) {
                LOGGER.info("null file.");
                return;
            }
            for (File fn : cfs) {
                if (fn.isFile()) {
                    try (FileInputStream fis = FileUtils.openInputStream(fn);
                         BufferedInputStream bis = new BufferedInputStream(fis); FileOutputStream fos =
                            FortifyUtils.newFileOutputStream(FileUtils.getFile(f2 + File.separator + fn.getName()));) {
                        byte[] bytes = new byte[UNIT];
                        int count = bis.read(bytes);
                        while (count != NumConstant.NUM_1_NEGATIVE) {
                            fos.write(bytes, 0, count);
                            count = bis.read(bytes);
                        }
                        fos.flush();
                    } catch (IOException e) {
                        LOGGER.error("copy method happen IOException !");
                    }
                } else {
                    File fb = FileUtils.getFile(f2 + File.separator + fn.getName());
                    if (!fb.mkdirs()) {
                        LOGGER.error("mkdir file failed.");
                    }
                    if (fn.listFiles() != null) { // 如果有子目录递归复制子目录！
                        copy(fn, fb);
                    }
                }
            }
        } else {
            fileRead(f1, f2);
        }
    }

    private void fileRead(File file, File f1) {
        try (InputStream fis = FortifyUtils.newFileInputStream(file); FileOutputStream fos =
            FortifyUtils.newFileOutputStream(FileUtils.getFile(f1 + File.separator + file.getName()))) {
            byte[] bytes = new byte[UNIT];
            int conut = fis.read(bytes);
            while (conut != NumConstant.NUM_1_NEGATIVE) {
                fos.write(bytes, 0, conut);
                conut = fis.read(bytes);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException fileRead ");
        } catch (IOException e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }

    /**
     * 讲一个文件夹下面的文件全部复制到另外一个文件夹
     *
     * @param path 需要复制的路径
     * @param path1 目标路径
     * @return boolean
     */
    private boolean ioCopy(String path, String path1) {
        boolean isFlag = false;
        File file = FileUtils.getFile(path);
        File file1 = FileUtils.getFile(path1);
        if (!file.exists()) {
            LOGGER.info("file does not exist");
        } else {
            LOGGER.info("File exists");
        }
        byte[] bytes = new byte[UNIT];
        if (file.isFile()) {
            try (InputStream is = FortifyUtils.newFileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(is);
                 FileOutputStream ps = FortifyUtils.newFileOutputStream(file1);) {
                int length = bis.read(bytes);
                while (length != NumConstant.NUM_1_NEGATIVE) {
                    ps.write(bytes, 0, length);
                    length = bis.read(bytes);
                }
            } catch (IOException e) {
                LOGGER.error("error: {}", e.getMessage());
            }
        } else if (file.isDirectory()) {
            if (!file.exists()) {
                boolean isRt = file.mkdirs();
                if (!isRt) {
                    LOGGER.error("mkdir file failed.");
                }
            }
            String[] lists = file.list();
            if (lists != null) {
                for (int ii = 0; ii < lists.length; ii++) {
                    ioCopy(path + File.separator + lists[ii], path1 + File.separator + lists[ii]);
                    isFlag = true;
                }
            }
        }
        return isFlag;
    }

    /**
     * 修改文件的名称
     *
     * @param fileList 要修改的文件
     * @return String 修改过后的文件的名称
     */
    private String updateFileName(List<String> fileList) {
        if (fileList != null && fileList.size() > 0) {
            String fileName = fileList.get(0);
            UUID uuid = UUID.randomUUID();
            String reg = "[\u4e00-\u9fa5]";
            Pattern pat = Pattern.compile(reg);
            Matcher mat = pat.matcher(fileName);
            String repickStr = mat.replaceAll(CommonSymbolicConstant.SYMBOL_BLANK);
            String newName = uuid + CommonSymbolicConstant.SYMBOL_BLANK + repickStr;
            File oldpathEn = FileUtils.getFile(analyse + File.separator + newName); // 从需要分析的文件夹下改名
            File oldpathCn = FileUtils.getFile(analyse + File.separator + fileName);
            boolean isFlag2 = oldpathCn.renameTo(oldpathEn);
            if (isFlag2) {
                LOGGER.info("Changed name to success");
            } else {
                LOGGER.info("Changed name to fail");
            }
            LOGGER.info("English name file into List");
            boolean isFlagdelete = oldpathCn.delete();
            if (isFlagdelete) {
                LOGGER.info("Delete the file after changing the name");
            } else {
                LOGGER.info("Delete the file after changing the name fail");
            }
            return analyse + File.separator + newName;
        }
        return CommonSymbolicConstant.SYMBOL_BLANK;
    }

    /**
     * 先删除该周期内的原始数据表，再动态创建表
     *
     * @param filePath 文件路径
     * @param periodStartDate 开始周期
     * @param preTableName 表名字
     */
    @Override
    public void creareOriginalAfterDropTbl(String filePath, Date periodStartDate, String preTableName) {
        Map<String, Object> conditions = new HashMap<String, Object>();

        String tabName = UtilMethod.getTableName(preTableName, periodStartDate);
        conditions.put(PATH, filePath);
        conditions.put(TAB_NAME, tabName);

        // 先删除源数据表
        originalDataDaoDropTable(conditions);

        // 创建原始数据表
        createOriginalDataTable(filePath, tabName);
    }

    private void originalDataDaoDropTable(Map<String, Object> condition) {
        try {
            originalDataDao.dropTable(condition);
        } catch (DataAccessException e) {
            LOGGER.error("showTableByname DataAccessException {} ", e.getMessage());
        }
    }

    /**
     * createOriginalDataTable
     *
     * @param filePath filePath
     * @param tabName tabName
     * @return boolean
     */
    @Override
    public boolean createOriginalDataTable(String filePath, String tabName) {
        String sqlStr = createTable(filePath);
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put(TAB_NAME, tabName);
        conditions.put("sqlStr", sqlStr);
        return originalDataDaoCreateOriginalData(conditions);
    }

    private boolean originalDataDaoCreateOriginalData(Map<String, Object> condition) {
        try {
            return originalDataDao.createOriginalData(condition) != NumConstant.NUM_1_NEGATIVE;
        } catch (DataAccessException e) {
            LOGGER.error("showTableByname DataAccessException {} ", e.getMessage());
        }
        return false;
    }

    private String createTable(String filePath) {
        String line = CommonSymbolicConstant.SYMBOL_BLANK;
        try (InputStream fileInputStream = FileUtils.openInputStream(FileUtils.getFile(filePath));
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, CommonConstant.CHARSET_GBK);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String lineTmp = FortifyUtils.readLine(bufferedReader);
            line = !StringUtil.isNull(lineTmp) ? lineTmp : line;
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UnsupportedEncodingException:{}", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("IOException:{}", e.getMessage());
        }
        String[] titleArrs = line.split(CommonSymbolicConstant.SYMBOL_SEPARATOR);
        StringBuilder sb = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        String title = CommonSymbolicConstant.SYMBOL_BLANK;
        for (int ii = 0; ii < titleArrs.length; ii++) {
            title = titleArrs[ii];
            sb.append(FILED);
            sb.append(ii + 1);
            sb.append(" varchar(100) NOT NULL COMMENT '");
            sb.append(title);
            sb.append("',");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 动态对TCP UDP 通过replace 替换时间格式中的斜杠为中杠
     *
     * @param tableName 表名
     * @param filedClass filedClass
     * @param fileList 文件列表
     * @param type TCP/UDP
     */
    private void operateTcpOrUdp(Object tableName, FiledClass filedClass, List<FiledInfo> fileList, String type) {
        if (CommonUtil.isNotNull(tableName) && CollectionUtil.isNotEmpty(fileList)) {
            Map<String, Object> conditions = new HashMap<String, Object>();
            conditions.put(TAB_NAME, tableName);
            conditions.put("changeFiled", filedClass.changeFiledSql(fileList));
            conditions.put("dropColumn", filedClass.dropColumnSql(fileList, type));
            originalDataDaoChangeFiled(conditions);
            originalDataDaoDropColumn(conditions);
            conditions.put("flowStartTime", "");
            conditions.put("flowEndTime", "");
            originalDataDaoFormatDateType(conditions);
        }
    }

    private void originalDataDaoChangeFiled(Map<String, Object> condition) {
        try {
            originalDataDao.changeFiled(condition);
        } catch (DataAccessException e) {
            LOGGER.error("changeFiled DataAccessException {} ", e.getMessage());
        }
    }

    private void originalDataDaoDropColumn(Map<String, Object> condition) {
        try {
            originalDataDao.dropColumn(condition);
        } catch (DataAccessException e) {
            LOGGER.error("dropColumn DataAccessException {} ", e.getMessage());
        }
    }

    private void originalDataDaoFormatDateType(Map<String, Object> condition) {
        try {
            originalDataDao.formatDateType(condition);
        } catch (DataAccessException e) {
            LOGGER.error("formatDateType DataAccessException {} ", e.getMessage());
        }
    }

    /**
     * 如库周期管理表
     *
     * @param periodStartDate date
     */
    private void insertPeriodManage(Date periodStartDate) {
        TblPeriodManage tblPeriodManage = new TblPeriodManage();
        tblPeriodManage.setIsAnalyseOver("0");
        tblPeriodManage.setPeriodStartTime(periodStartDate);
        try {
            periodManageDao.insert(tblPeriodManage);
        } catch (DataAccessException e) {
            LOGGER.error("insert DataAccessException {} ", e.getMessage());
        }
    }

    /**
     * 动态创建tableName 表
     *
     * @param path 路径
     * @param periodStartDate 时间
     * @param tableName 表名
     */
    private void createOriginal(String path, Date periodStartDate, String tableName) {
        if (CommonUtil.isNotNull(path, periodStartDate)) {
            creareOriginalAfterDropTbl(path, periodStartDate, tableName);
        } else {
            LOGGER.error("create tableName fail {} because parameter contains null ", tableName);
        }
    }

    /**
     * 对pathList进行过滤，只保留最新的文件名称
     *
     * @param pathList 路径列表
     */
    private void getNewestPath(List<String> pathList) {
        if (!CollectionUtil.isNotEmpty(pathList)) {
            String tcpNewestTime = getNewest(pathList);
            filterByNewest(pathList, tcpNewestTime);
        }
    }

    private String getNewest(List<String> list) {
        if (!CheckUtils.isNullOrBlank(list)) {
            String regex = "[0-9]{4}(-[0-9]{2}){2} ([0-9]{2}[.]){2}[0-9]{2}\\.[csvCSV]{3}";
            Pattern pattern = Pattern.compile(regex);
            List<String> tempList = new ArrayList<String>();
            for (int ii = 0; ii < list.size(); ii++) {
                Matcher matcher = pattern.matcher(Normalizer.normalize(list.get(ii), Form.NFKC));
                if (matcher.find()) {
                    tempList.add(matcher.group());
                }
            }
            Collections.sort(tempList);
            if (CheckUtils.isNullOrBlank(tempList)) {
                return CommonSymbolicConstant.SYMBOL_BLANK;
            } else {
                return tempList.get(tempList.size() - 1);
            }
        } else {
            return CommonSymbolicConstant.SYMBOL_BLANK;
        }
    }

    private void filterByNewest(List<String> list, String newest) {
        if (CollectionUtil.isEmpty(list) || StringUtil.isEmpty(newest)) {
            return;
        }
        int size = list.size();
        List<String> removeList = new ArrayList<String>();
        for (int ii = 0; ii < size; ii++) {
            if (list.get(ii).indexOf(newest) == NumConstant.NUM_1_NEGATIVE) {
                removeList.add(list.get(ii));
            }
        }
        for (int ii = 0; ii < removeList.size(); ii++) {
            list.remove(removeList.get(ii));
        }
    }

    /**
     * 通过构造器进行向TCP UDP进行赋值
     *
     * @version 1.0, 2018年7月30日
     * @since 2018-07-30
     */
    static class FiledClass {
        // TCP
        private List<String> allField4Tcps = null;

        // UDP
        private List<String> allField4Udps = null;

        // 通过构造器进行向TCP UDP进行赋值，考虑到可配置型，没有采用静态方式
        FiledClass(Integer allFieldNum4Tcp, Integer allFieldNum4Udp) {
            allField4Tcps = new ArrayList<String>();
            for (int ii = 0; ii < allFieldNum4Tcp; ii++) {
                allField4Tcps.add(FILED + (ii + 1));
            }
            allField4Udps = new ArrayList<String>();

            for (int ii = 0; ii < allFieldNum4Udp; ii++) {
                allField4Udps.add(FILED + (ii + 1));
            }
        }

        /**
         * 根据获取的实体对象，拼接需要修改列名称的sql语句
         *
         * @param allChangeFiled 所有需要修改的列名
         * @return String sql
         */
        public String changeFiledSql(List<FiledInfo> allChangeFiled) {
            if (!CheckUtils.isNullOrBlank(allChangeFiled)) {
                StringBuilder stringBuilder = new StringBuilder("");
                for (int ii = 0; ii < allChangeFiled.size(); ii++) {
                    stringBuilder.append(CHANGE)
                        .append(CommonSymbolicConstant.SPACE)
                        .append(allChangeFiled.get(ii).getRef())
                        .append(CommonSymbolicConstant.SPACE)
                        .append(allChangeFiled.get(ii).getName())
                        .append(CommonSymbolicConstant.SPACE)
                        .append(allChangeFiled.get(ii).getDbtype())
                        .append(CommonSymbolicConstant.SYMBOL_SEPARATOR);
                }
                return stringBuilder.substring(0, stringBuilder.lastIndexOf(CommonSymbolicConstant.SYMBOL_SEPARATOR));
            } else {
                return CommonSymbolicConstant.SYMBOL_BLANK;
            }
        }

        /**
         * 根据获取的实体对象，拼接需要删除的列的sql语句
         *
         * @param allChangeFiled 所有删除的列名
         * @param tcpOrUdp 协议类型
         * @return String sql
         */
        public String dropColumnSql(List<FiledInfo> allChangeFiled, String tcpOrUdp) {
            if (!CheckUtils.isNullOrBlank(allChangeFiled) && !CheckUtils.isNullOrBlank(tcpOrUdp)) {
                List<String> allFieldCopys = new ArrayList<String>();
                if (TCP.equals(tcpOrUdp)) {
                    allFieldCopys.addAll(allField4Tcps);
                } else if (UDP.equals(tcpOrUdp)) {
                    allFieldCopys.addAll(allField4Udps);
                } else {
                    LOGGER.error("dropColumnSql not support");
                    return "";
                }
                for (int ii = 0; ii < allChangeFiled.size(); ii++) {
                    allFieldCopys.remove(allChangeFiled.get(ii).getRef());
                }
                StringBuilder stringBuilder = new StringBuilder("");
                for (int ii = 0; ii < allFieldCopys.size(); ii++) {
                    stringBuilder.append(DROP_COLUMN);
                    stringBuilder.append(CommonSymbolicConstant.SPACE);
                    stringBuilder.append(allFieldCopys.get(ii));
                    stringBuilder.append(CommonSymbolicConstant.SYMBOL_SEPARATOR);
                }
                return stringBuilder.substring(0, stringBuilder.lastIndexOf(CommonSymbolicConstant.SYMBOL_SEPARATOR));
            } else {
                return "";
            }
        }
    }
}
