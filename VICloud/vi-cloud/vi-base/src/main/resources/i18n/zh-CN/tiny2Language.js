// 控件的简体中文词条设置
define([], function () {
    return {
        // 公共词条
        more_title: '更多',
        ok_btn: '确认',
        cancel_btn: '取消',
        // actionMenu组件词条
        actionmenu_operate_title: '操作',
        // message控件词条
        msg_prompt_title: '提示',
        msg_warn_title: '警告',
        msg_confirm_title: '确认',
        msg_error_title: '错误',
        // 时间日期组件
        date_format: 'yyyy-MM-dd',
        time_format: 'HH:mm',
        date_clear_btn: '清除',
        date_today_btn: '今天',
        date_prev_month_title: '上月',
        date_next_month_title: '下月',
        date_week_start_value: 1, // 不需翻译
        date_week_names_abb: ['日', '一', '二', '三', '四', '五', '六'],
        date_week_names_title: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
        date_month_names_abb: ['1 月', '2 月', '3 月', '4 月', '5 月', '6 月', '7 月', '8 月', '9 月', '10 月', '11 月', '12 月'],
        btn_slider_arr: ['1', '2', '3', '4', '5', '6', '7', '8', '9个月', '1 年', '2 年', '3 年'],
        date_year_suffix_label: '年',
        date_range_begin_label: '开始日期',
        date_range_end_label: '结束日期',
        datetime_range_time_label: '时间：',
        // 分页
        page_goto_label: '跳转', // Pagination控件：goto跳转按钮的title属性值
        page_prev_title: '上一页', // Pagination控件：上一页按钮的title属性值
        page_next_title: '下一页', // Pagination控件：下一页按钮的title属性值
        page_total_label: '总条数: ', // Pagination控件：显示消息总条数部分前边的文本
        // 文件上传
        upload_select_file_tip: '选择文件',
        upload_file_btn: '上传文件',
        upload_file_btn_tip: '未发现待上传文件，请先在左侧选择。',
        upload_add_file_btn: '添加文件',
        upload_wait_info: '等待上传',
        upload_success_info: '文件导入成功，数据在下个分析周期生效。',
        upload_error_info: '上传失败',
        upload_add_success_general_info: '已添加{0}个文件',
        upload_single_add_success_general_info: '已添加文件',
        upload_single_uploading_general_info: '正在上传数据，请稍后……',
        upload_uploading_general_info: '{0} 正在上传数据，请稍后……',
        upload_single_error_general_info: '上传失败!',
        upload_error_general_info: '{0}个文件上传失败!',
        upload_remove_file_single: '清空选择',
        upload_remove_files: '清空选择',
        upload_cancel_files: '取消上传',
        upload_reload_files: '重新上传',
        // 校验
        valid_required_info: '输入不能为空。',
        valid_maxSize_info: '输入长度不能超过{0}。',
        valid_minSize_info: '输入长度不能小于{0}。',
        valid_rangeSize_info: '输入长度范围为{0}到{1}。',
        valid_maxValue_info: '输入值不能超过{0}。',
        valid_minValue_info: '输入值不能小于{0}。',
        valid_rangeValue_info: '输入值必须在{0}到{1}之间。',
        valid_regularCheck_info: '输入值无效。',
        valid_contains_info: '输入值必须包含有字符{0}。',
        valid_notContains_info: '输入值不能含有非法字符{0}。',
        valid_checkScriptInfo_info: '输入值不能含有script标签。',
        valid_equal_info: '输入值必须等于{0}。',
        valid_notEqual_info: '输入值不能等于{0}。',
        valid_port_info: '端口号为0到65535的整数。',
        valid_path_info: '输入值未满足路径格式要求。',
        valid_email_info: '输入email地址无效。',
        valid_date_info: '输入日期无效。',
        valid_url_info: '输入URL无效。',
        valid_integer_info: '输入值不是有效整数。',
        valid_number_info: '输入值不是有效数字。',
        valid_digits_info: '输入值不是有效数字字符。',
        valid_ipv4_info: '输入值不是有效IPV4地址。',
        valid_ipv6_info: '输入值不是有效IPV6地址。',
        valid_rangeSize_msg: '{0}~{1}个字符。',
        valid_charTypeNum_msg: '至少包含以下字符中的{0}种：大写字母、小写字母、数字、特殊字符`~!@#$%^&*()-_=+\|[{}];:\'\",<.>/?  和空格。',
        valid_notEqualPosRev_msg: '不能与用户名或倒序的用户名相同。',
        valid_pwd_info: '密码输入不符合要求，请重新输入。',
        valid_pwd_sec: '安全程度:',
        valid_pwd_sec_level_low: '弱',
        valid_pwd_sec_level_medium: '中',
        valid_pwd_sec_level_high: '强',
        valid_phone_info: '输入不是有效的电话号码。',
        valid_phone_short_info: '输入电话号码位数太少。',
        valid_phone_long_info: '输入电话号码位数太多。',
        // charts 组件
        loadingText: '数据加载中...',
        // intro
        intro_skip: '跳过',
        intro_previous: '上一个',
        intro_next: '下一个',
        intro_finish: '立即体验',

        //multiSelect
        select_all: '(全选)',

        nodata_text: '暂无数据',
        // wizard组件
        wizard_required_msg: '必选项',
        // tiColsToggle组件词条
        table_cols_toggle_modal_title: '自定义列表字段',
        table_cols_toggle_modal_info: '请选择您想显示的列表详细信息，最多勾选9个字段，已勾选{0}个。'
    }
});