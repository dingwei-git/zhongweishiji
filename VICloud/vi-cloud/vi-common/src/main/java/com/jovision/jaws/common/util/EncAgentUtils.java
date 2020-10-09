package com.jovision.jaws.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jovision.jaws.common.config.redis.ApplicationConfig;
import com.jovision.jaws.common.constant.BusinessAgentTypeEnum;
import com.jovision.jaws.common.dto.DecprytConfigurationFieldsDto;
import com.jovision.jaws.common.dto.GetMasterKeyListDto;
import com.jovision.jaws.common.po.FieldsPo;
import com.jovision.jaws.common.po.KeyValuePo;
import com.jovision.jaws.common.po.NamespacePo;
import com.jovision.jaws.common.vo.DecprytConfigurationFieldsVo;
import com.jovision.jaws.common.vo.GetMasterKeyListVo;
import com.jovision.jaws.common.vo.MasterKeyVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : fengyefeiluo
 * @ClassName EncAgentUtils
 * @Version 1.0
 * @Date : 2020/1/20 15:02
 * @UpdateDate : 2020/1/20 15:02
 * @Version V1.0.0
 **/
@Slf4j
public class EncAgentUtils {
    public static List<MasterKeyVo> MasterKeyVoList = new ArrayList<MasterKeyVo>();//存放全局用户主密钥
    public static MasterKeyVo masterKeyVoLatest = null;//最新可用主密钥

    private static String decMethod = "/v1/amah_agent/decpryt_configuration_fields";
    private static String mkListMethod = "/v1/amah_agent/get_master_key_list";
    /**
     * 从agent获取配置文件解密信息
     */
    public static String getDecodeConfig(String confName,String ConfEnc){
        KeyValuePo keyValuePo = new KeyValuePo(confName,ConfEnc);
        List kvList = new ArrayList();
        kvList.add(keyValuePo);
        FieldsPo fieldsPo = new FieldsPo(kvList);
        DecprytConfigurationFieldsDto decprytConfigurationFieldsDto = new DecprytConfigurationFieldsDto(decMethod, new SecureRandom().nextInt(1000000) + "",fieldsPo);
        DecprytConfigurationFieldsVo decprytConfigurationFieldsVo = (DecprytConfigurationFieldsVo)commonSock(decprytConfigurationFieldsDto, BusinessAgentTypeEnum.DECPRYT_CONFIGURATION_FIELDS.getCode());
        //System.out.println("【zk】:" + JSONObject.toJSONString(decprytConfigurationFieldsVo));
        if(null != decprytConfigurationFieldsVo  && null != decprytConfigurationFieldsVo.getResult()){
            List<KeyValuePo> fieldsList = decprytConfigurationFieldsVo.getResult().getFields();
            if(null != fieldsList && fieldsList.size()>0){
                if(confName.equals(fieldsList.get(0).getKey())){
                    return fieldsList.get(0).getValue();
                }
            }
        }
        return  null;
    }

    /**
     * 获取主密钥
     */
    public static GetMasterKeyListVo getEncryKeys(){
        GetMasterKeyListDto getMasterKeyListDto = new GetMasterKeyListDto(mkListMethod,new SecureRandom().nextInt(1000000) + "",new NamespacePo("user"));
        GetMasterKeyListVo getMasterKeyListVo = (GetMasterKeyListVo)commonSock(getMasterKeyListDto,BusinessAgentTypeEnum.GET_MASTER_KEY_LIST.getCode());
        //System.out.println("【zk:】" + JSONObject.toJSONString(getMasterKeyListVo));
        return  getMasterKeyListVo;
    }

    public static Object commonSock(Object info,int methodType){
        File socketFile = new File("/opt/holo/amah_agent/amah_agent.sock");
        AFUNIXSocket sock = null;
        try {
            sock = AFUNIXSocket.newInstance();
            sock.connect(new AFUNIXSocketAddress(socketFile));

            OutputStream os = sock.getOutputStream();
            PrintWriter pw = new PrintWriter(os);

            InputStream is=sock.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));

            String content= JSONObject.toJSONString(info) + "\n";
            //System.out.println("发送的数据："+ content);

            pw.write(content);
            pw.flush();
            sock.shutdownOutput();

            String reply=null;
            String retStr = null;
            while(!((reply=br.readLine())==null)){
                //log.info("接收服务器的信息："+reply);
                retStr = reply;
            }
            br.close();
            is.close();
            pw.close();
            os.close();
            sock.close();
            if(StringUtils.isNotBlank(retStr)){
                if(BusinessAgentTypeEnum.DECPRYT_CONFIGURATION_FIELDS.getCode() == methodType){
                    return JSON.parseObject(retStr, DecprytConfigurationFieldsVo.class);
                }else if(BusinessAgentTypeEnum.GET_MASTER_KEY_LIST.getCode() == methodType){
                    return JSON.parseObject(retStr, GetMasterKeyListVo.class);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取最新的MK,【加密】时使用
     * @return
     */
    public static MasterKeyVo getLastestMK(List<MasterKeyVo> masterKeyVoList){
        for(MasterKeyVo masterKeyVo:masterKeyVoList){
            if(masterKeyVo.getLatest()){
                //如果是最新，直接返回即可
                return masterKeyVo;
            }
        }
        return null;
    }
//    /**
//     * 获取最新的MK,【加密】时使用,暂时不上众测版本
//     * @return
//     */
//    public static MasterKeyVo getLastestMKNew(List<MasterKeyVo> masterKeyVoList){
//        for(MasterKeyVo masterKeyVo:masterKeyVoList){
//            if(masterKeyVo.getLatest()){
//                if(null == masterKeyVoLatest){
//                    //如果没有存最新秘钥，说明首次使用，直接用主密钥即可
//                    masterKeyVoLatest = masterKeyVo;
//                    return masterKeyVoLatest;
//                }
//                if(System.currentTimeMillis()/1000 > masterKeyVo.getEffective_time()){
//                    //effective_time初始值为0，后续代表生效时间，单位是秒
//                    masterKeyVoLatest = masterKeyVo;
//                }
//            }
//        }
//        return masterKeyVoLatest;
//    }

    /**
     * 根据主密钥mk_id获取对应的主秘钥，【解密】时使用
     * @param mk_id
     * @return
     */
    public static MasterKeyVo getMK(String mk_id){
        for(MasterKeyVo masterKeyVo:MasterKeyVoList){
            if(mk_id.equals(masterKeyVo.getMk_id())){
                //找到了，返回即可
                return masterKeyVo;
            }
        }
        return null;
    }

    /**
     * 本地加载主密钥，用于本地测试时使用
     */
    public static void localLoadMasterKey(){
        //替换成对应环境的主密钥即可
        MasterKeyVo masterKeyVo1 = new MasterKeyVo("20200201-2","qwertyuiopasdfghjklzxcvbnm",false);
        MasterKeyVo masterKeyVo2 = new MasterKeyVo("20200201-1","masterkey123456789",false);
        MasterKeyVo masterKeyVo3 = new MasterKeyVo("20200312-2","xYDnpmUBnoDgA1YQ",false);
        MasterKeyVo masterKeyVo4 = new MasterKeyVo("20200312-1","AqiumoVz47THLRBP",false);
        MasterKeyVo masterKeyVo5 = new MasterKeyVo("20200312-3","sUy0zQJK3qE3W5Dl",true);
        MasterKeyVo masterKeyVo6 = new MasterKeyVo("20200311-1","X42wmqrSnihvDbVe",false);
        MasterKeyVo masterKeyVo7 = new MasterKeyVo("20200409-1","x17WG9eoW7GghFlIkTIph9ykJUnxliOF",true);

        MasterKeyVoList.add(masterKeyVo1);
        MasterKeyVoList.add(masterKeyVo2);
        MasterKeyVoList.add(masterKeyVo3);
        MasterKeyVoList.add(masterKeyVo4);
        MasterKeyVoList.add(masterKeyVo5);
        MasterKeyVoList.add(masterKeyVo6);
        MasterKeyVoList.add(masterKeyVo7);

        //将用户jwt密钥、服务间jwt密钥、tiken秘钥放入内存（根据对应的环境）
        ApplicationConfig.setConfig("jwt.token.secret.current","fW8k7lEKAtLaK1aqQGHWteoMj9LUMx3q");
        ApplicationConfig.setConfig("jwt.token.secret.old","YyRS6lSTm2OBWsQqxG9lI5zCuc9linOq");
        ApplicationConfig.setConfig("jwt.saas.secret.current","1oANX7VCv7Kan74BCa7dnQnZv3iNeZGd");
        ApplicationConfig.setConfig("jwt.saas.secret.old","WISwsZXW5OBp9bySLyYBtN3Jk2CgHG82");
        ApplicationConfig.setConfig("jwt.tiken.secret.current","42iJD51TVYr8Nvprmm2");
        ApplicationConfig.setConfig("jwt.tiken.secret.old","32iJD51TVYr8Nvprmm2");
        ApplicationConfig.setConfig("huawei.appSecret","c0d2dd91a451cb8364afd398eec4afa58136b12ccd29ef885f6828f0caf01d32");
        ApplicationConfig.setConfig("apple.appSecret","kpNYI6YX");
        ApplicationConfig.setConfig("verification.code.expire","600");
        ApplicationConfig.setConfig("verification.code.locking-expire","60");
        ApplicationConfig.setConfig("user.password.maxRetryCount","3");
        ApplicationConfig.setConfig("user.password.duration","900");
        ApplicationConfig.setConfig("user.password.salt-byte-size","8");
        ApplicationConfig.setConfig("user.password.hash-bit-size","256");
        ApplicationConfig.setConfig("user.password.pbkdf2-iterations","1000");
        ApplicationConfig.setConfig("user.password.minLength","6");
        ApplicationConfig.setConfig("user.password.maxLength","20");
        ApplicationConfig.setConfig("spring.redis.redis-jwt-token.timeout","3");
        ApplicationConfig.setConfig("spring.redis.redis-jwt-tiken.timeout","3");
        ApplicationConfig.setConfig("spring.redis.redis-verification-code.timeout","3");
        ApplicationConfig.setConfig("spring.redis.redis-user-locking.timeout","3");
        ApplicationConfig.setConfig("spring.redis.redis-verification-code-locking.timeout","3");
        ApplicationConfig.setConfig("spring.redis.redis-add-device-locking.lockingTimeout","60");
        ApplicationConfig.setConfig("spring.redis.redis-add-device-locking.addDeviceErrorTimeout","900");

        ApplicationConfig.setConfig("jwt.token.expire","3000");
        ApplicationConfig.setConfig("jwt.tiken.expire","259200");
        ApplicationConfig.setConfig("jwt.saas.expire","86400");
        ApplicationConfig.setConfig("spring.application.name","localTest");

        ApplicationConfig.setConfig("share.device.limit",10);
        ApplicationConfig.setConfig("share.user.limit",10);

        ApplicationConfig.setConfig("user.password.error-duration",86400);
        ApplicationConfig.setConfig("user.password.duration",300);
        ApplicationConfig.setConfig("user.password.maxRetryCount",5);
        ApplicationConfig.setConfig("user.modifyPwd.maxRetryCount",5);

        ApplicationConfig.setConfig("gipc-white-list","ptz_move_start/ptz_move_stop/ptz_fi_start/ptz_fi_stop/dev_get_info/dev_update_check/osd_get_param/snapshot_get_base64/heatmap_space/heatmap_time/people_count_day/people_count_days/people_count_year");
        ApplicationConfig.setConfig("user.cancel.maxInterval","365");
        ApplicationConfig.setConfig("share.add.expire",900);

    }

}
