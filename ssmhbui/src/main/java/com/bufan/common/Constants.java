package com.bufan.common;

/**
 * 提示信息类
 *
 * @author LinQiang
 */
public class Constants {

    public static String DOMAIN;
    public static String cookieName;
    //存入session或cookie中的用户名:ZHG_NAME
    public static String USER_NAME_COOKIE = "44243bc48dfc619a9fd8ce73a9ad3a9d";
    //存入session或cookie中的密码:ZHG_PWD
    public static String USER_PWD_COOKIE = "1ff4a0287cdc405e8fd64cc619e30834";
    //存入session或cookie中的密码
    public static String USER_SESSION = "USER_SESSION";
    //SESSION_ID
    public static String COOKIE_JSESSIONID = "JSESSIONID";
    //默认配置文件路径
    public static String DEFAULT_PROPERTIES_PATH = "properties/config.properties";

    /**
     * 请求类型
     */
    public static enum REQUEST_METHOD {
        post, get, POST, GET
    };

    /**
     * 信息
     */
    public static enum MSG {
        MSG_ERROR_REGISTER_INFO1("请选择注册类型"),
        CK_USERNAME_NULL("用户名不能为空"),CK_REGISTER_NULL("用户注册检查失败"),CK_USERNAME_REGEX_ERROR("由字母数字下划线组成且开头必须是字母，不能超过16位"),
        CK_USERNAME_HAVE("用户名已经被注册"), CK_USERNAME_NO("用户名可用"),
        CK_EMAIL_NULL("邮箱不能为空"),CK_EMAIL_REGEX_ERROR("邮箱必须包含@符号；必须包含点；点和@之间必须有字符"),
        CK_EMAIL_HAVE("邮箱已经被注册"), CK_EMAIL_NO("邮箱可用"),
        CK_CELLPHONE_NULL("手机号不能为空"),CK_CELLPHONE_REGEX_ERROR("手机号全数字，长度为11位"),
        CK_CELLPHONE_HAVE("手机号已经被注册"), CK_CELLPHONE_NO("手机号可用"),
        CK_IDCARD_NULL("身份证不能为空"),CK_IDCARD_REGEX_ERROR("身份证全数字，长度为15|16位"),
        CK_IDCARD_HAVE("身份证已经被绑定"), CK_IDCARD_NO("身份证可用"),
        CK_QQ_NULL("qq不能为空"),CK_QQ_REGEX_ERROR("qq全数字，"),
        CK_QQ_HAVE("qq已经被绑定"), CK_QQ_NO("qq可用"),
        CK_WX_NULL("微信不能为空"),CK_WX_REGEX_ERROR("微信，"),
        CK_WX_HAVE("微信已经被绑定"), CK_WX_NO("微信可用"),
        CK_USERNAME_NULL_FALL("请填写用户名"), CK_PASSWORD_NULL_FALL("参数错误"),
        REGISTER_USER_FAIL("注册用户失败"),
        SEND_EMAIL_FAIL("发送邮件失败"),SEND_EMAIL_SUCCESS("发送邮件成功"),
        SEND_EMAIL_URL_FAIL("请求路径不对"),
        NET_SYSTEM_ERROR("网络异常或系统错误"), PARAM_ERROR("参数错误"),
        LOGIN_FAIL("登录失败"), LOGIN_SUCCESS("登录成功"),
        UPDATE_FAIL("更新失败"), UPDATE_SUCCESS("更新成功"),
        UPDATE_FAIL_PASSWORD("修改密码失败"), UPDATE_SUCCESS_PASSWORD("修改密码成功"),
        UPLOAD_FAIL("上传失败"), UPLOAD_SUCCESS("上传成功"), UPLOAD_NOUPDATE("没有上传"),
        QUERY_FAIL("查询失败"), QUERY_SUCCESS("查询成功"), QUERY_SUCCESS_NODATA("查询成功但没有数据"),
        QUERY_DATAPARM_NULL_FALL("参数获取失败"), QUERY_DATA_PARM_NULL_SUCCESS("参数获取成功"),
        DEL_FAIL("删除失败"), DEL_SUCCESS("删除成功"),
        DEL_FAIL_PARENT("删除失败,该节点包含了多个子节点。"),
        ADD_FAIL("新增失败"), ADD_SUCCESS("新增成功"), UNDEFINED("undefined"),
        REGISTER_FAIL("注册失败"), REGISTER_SUCCESS("注册成功"),
        STOP_FALL("停用失败"), STOP_SUCCESS("停用成功了"),
        UPDATE_PASSWORD_SUCCESS("修改密码成功了"), UPDATE_PASSWORD_ERROR("修改密码失败了"),
        Send_Url_fall("转发成功了"), Send_Url_Success("转发失败"),
        SYNCDATAFALL("同步数据失败了"), SYNCDATASUCCESS("同步数据成功了"),
        GETUSERINFOFALL("获取用户数据失败"), GETUSERINFOSUCCESS("获取用户数据成功"),
        EXCEL_FALL1("导入失败"), EXCEL_FSUCCESS1("导入成功"),
        EXCEL_import_FALL1("格式不对,请选择.xls文件格式的文件"), EXCEL_import_SUCCESS("文件格式正确"),
        EXCEL_EXPORT_FALL("导出失败"), EXCEL_EXPORT_SUCCESS("导出成功"),
        DATA_FALL("参数未传递"), DATA_SUCCESS("参数传递成功"),
        COURSELIBARY_FALL("尚未排课"), COURSELIBARY_SUCCESS("已经排课"),
        CLEAR_CHCACHE_FALL("缓存清理失败"), CLEAR_CHCACHE_SUCCESS("缓存清理成功"),
        TONGBU_FALL("同步失败"), TONGBU_SUCCESS("同步成功"),
        BASEDAO_ADD_FALL("添加失败"), BASEDAO_ADD_SUCCESS("添加成功"),
        BASEDAO_UPDATE_FALL("修改失败"), BASEDAO_UPDATE_SUCCESS("修改成功"),
        BASEDAO_DELETE_FALL("删除失败"), BASEDAO_DELETE_SUCCESS("删除成功"),
        BASEDAO_QUERY_FALL("查询失败"), BASEDAO_QUERY_SUCCESS("查询成功"),
        BASEDAO_CHECK_FALL("检查失败"), BASEDAO_CHECK_SUCCESS("检查成功"),
        START_FALL("启用失败"), START_SUCCESS("启用成功了");
        private String value;
        // 构造方法

        private MSG(String value) {
            this.value = value;
        }
        // 普通方法

        @Override
        public String toString() {
            return this.value;
        }
    };

    /**
     * 数据状态标签
     *
     * @author xiaosun
     *
     */
    public static enum DATACODE {
        DATA_DELETE("2", 1),
        DATA_USE("1", 2);//数据使用中
        // 成员变量
        private String value;
        private int num;
        // 构造方法

        private DATACODE(String value, int num) {
            this.value = value;
            this.setNum(num);
        }
        // 普通方法

        @Override
        public String toString() {
            return this.value;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    };

    /**
     * 检测为空
     *
     * @param obj
     * @return
     */
    public static boolean check_ARRAYNULL(Object obj) {
        boolean blog = false;
        //验证数组是否为空
        try {
            if (obj == null || obj.equals("") || obj.equals(null) || obj == "") {
                blog = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blog;
    }
    /**
     * 集合
     */
    public static String LIST = "list";
    /**
     * 分页实体
     */
    public static String PAGE_UTIL = "pageUtil";
    /**
     * 单个数据
     */
    public static String DATA = "data";
    /*返回mapsql的键*/
    public static String COUNTSQL = "countsql";
    public static String DATASQL = "datasql";
    public static String CONNTYPE = "type";

    public String getDOMAIN() {
        return DOMAIN;
    }

    public void setDOMAIN(String dOMAIN) {
        DOMAIN = dOMAIN;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        Constants.cookieName = cookieName;
    }

    /**
     * 页面跳转
     */
    public static enum PAGE {
        //登录成功页面
        PAGE_LOGIN_SUCCESS("/view/test/success.jsp"),
        //登录页面
        PAGE_LOGIN_RESET("/view/test/login_register.jsp"),
        //注册页面
        PAGE_REGISTER_RESET("/view/test/login_register.jsp"),
        //前台用户个人中心
        PAGE_PRE_USERCENTER("/user/usercenter/"),
        PAGE_PRE_USERCENTOR("/view/test/preusercenter.jsp"),
        //前台用户登录中心
        PAGE_PRE_USERCLOGIN("/view/test/login_register.jsp"),
        //词典页面跳转
        PAGE_DICT_AR_ZH_HOME("/view/dictionary-home.jsp"),
        //查词页面跳转
        PAGE_DICT_AR_ZH_BASIC("/view/dictionary-basic.jsp"),
        //领域页面查词
        PAGE_DICT_AR_ZH_DOMAIN("/view/dictionary-domain.jsp"),
        //后台登录页面
        PAGE_ADMIN_LOGIN("/view/admin/login.jsp"),
        //后台首页
        PAGE_ADMIN_INDEX("/view/success.jsp");
        // 成员变量
        private String value;

        private PAGE(String value) {
            this.value = value;
        }

        // 普通方法
        @Override
        public String toString() {
            return this.value;
        }
    }
}
