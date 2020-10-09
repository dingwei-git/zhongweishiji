// 控件的美式英文词条设置
define([], function () {
    return {
        // 公共词条
        more_title: 'More',
        ok_btn: 'OK',
        cancel_btn: 'Cancel',
        // actionMenu组件词条
        actionmenu_operate_title: 'Operation',
        // message控件词条
        msg_prompt_title: 'Information',
        msg_warn_title: 'Warning',
        msg_confirm_title: 'Confirm',
        msg_error_title: 'Error',
        // 时间日期组件
        date_format: 'MM/dd/yyyy',
        time_format: 'HH:mm',
        date_clear_btn: 'Clear',
        date_today_btn: 'Today',
        date_prev_month_title: 'Prev',
        date_next_month_title: 'Next',
        date_week_start_value: 0, // 不需翻译
        date_week_names_abb: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
        date_week_names_title: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
        date_month_names_abb: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
        "btn_slider_arr": ['1', '2', '3', '4', '5', '6', '7', '8', '9 months', '1 year', '2 years', '3 years'],
        date_year_suffix_label: '',
        date_range_begin_label: 'Start Date',
        date_range_end_label: 'End Date',
        datetime_range_time_label: 'Time：',
        // 分页
        page_goto_label: 'Go', // Pagination控件：goto跳转按钮的title属性值
        page_prev_title: 'Previous', // Pagination控件：上一页按钮的title属性值
        page_next_title: 'Next', // Pagination控件：下一页按钮的title属性值
        page_total_label: 'Total Records: ', // Pagination控件：显示消息总条数部分前边的文本
        // 文件上传
        upload_select_file_tip: 'Select File',
        upload_file_btn: 'Upload',
        upload_file_btn_tip: 'Select the file you want to upload.',
        upload_add_file_btn: 'Add File',
        upload_wait_info: 'Wait',
        upload_success_info: 'File uploaded successfully.',
        upload_error_info: 'Error',
        upload_add_success_general_info: 'You have added {0} files.',
        upload_single_add_success_general_info: 'You have added a file.',
        upload_single_uploading_general_info: 'Uploading',
        upload_uploading_general_info: 'Uploading: {0}',
        upload_single_error_general_info: 'File could not be uploaded.',
        upload_error_general_info: '{0} files could not be uploaded.',
        upload_remove_file_single: 'Clear',
        upload_remove_files: 'Clear All',
        upload_cancel_files: 'Cancel',
        upload_reload_files: 'Upload Again',
        // 校验
        valid_required_info: 'This field cannot be left blank.',
        valid_maxSize_info: 'Enter a maximum of {0} characters.',
        valid_minSize_info: 'Enter at least {0} characters.',
        valid_rangeSize_info: 'Enter {0} to {1} characters.',
        valid_maxValue_info: 'Enter a value less than or equal to {0}.',
        valid_minValue_info: 'Enter a value greater than or equal to {0}.',
        valid_rangeValue_info: 'Enter a value from {0} to {1}.',
        valid_regularCheck_info: 'Invalid value.',
        valid_contains_info: 'The value must contain the following characters: {0}.',
        valid_notContains_info: 'The value cannot contain the following invalid characters: {0}.',
        valid_checkScriptInfo_info: 'The value cannot contain script tags.',
        valid_equal_info: 'The value must be {0}.',
        valid_notEqual_info: 'The value cannot be {0}.',
        valid_port_info: 'Enter an integer from 0 to 65535.',
        valid_path_info: 'Enter a value conforming to the path format requirement.',
        valid_email_info: 'Enter a valid email address.',
        valid_date_info: 'Enter a valid date.',
        valid_url_info: 'Enter a valid URL.',
        valid_integer_info: 'Enter a valid integer.',
        valid_number_info: 'Enter a valid number.',
        valid_digits_info: 'Enter a valid number.',
        valid_ipv4_info: 'Enter a valid IPv4 address.',
        valid_ipv6_info: 'Enter a valid IPv6 address.',
        valid_rangeSize_msg: 'Must be {0} to {1} characters long.',
        valid_charTypeNum_msg: 'Must contain at least {0} of the following character types: uppercase letters, lowercase letters, digits, and special characters ( `~!@#$%^&*()-_=+\|[{}];:\'\",<.>/? and spaces).',
        valid_notEqualPosRev_msg: 'Cannot be the username or the username spelled backwards.',
        valid_pwd_info: 'Invalid password.',
        valid_pwd_sec: 'Password Strength:',
        valid_pwd_sec_level_low: 'Weak',
        valid_pwd_sec_level_medium: 'Medium',
        valid_pwd_sec_level_high: 'Strong',
        valid_phone_info: 'Enter a valid phone number.',
        valid_phone_short_info: 'Invalid phone number.',
        valid_phone_long_info: 'Invalid phone number.',
        // charts 组件
        loadingText: 'Loading data...',
        intro_skip: 'Skip',
        intro_previous: 'Previous',
        intro_next: 'Next',
        intro_finish: 'Finish',

        //multiSelect
        select_all: '(Select all)',

        nodata_text: 'No data available.',

        // wizard
        wizard_required_msg: 'Mandatory',
        // tiColsToggle组件词条
        table_cols_toggle_modal_title: "Show/Hide Column",
        table_cols_toggle_modal_info: 'Select the columns you want to display. You can select up to 9 columns, and you have selected {0} columns. '
    }
});