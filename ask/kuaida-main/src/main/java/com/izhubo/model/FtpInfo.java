package com.izhubo.model;

public class FtpInfo implements java.io.Serializable {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 2811503978526095654L;
	/** 文件原名 */
	private String original_file_name="";
	/** 绝对地址 */
	private String absolute_url ="";
	/** 文件名 */
	private String _id = "";
	/** 文件大小 */
	private Long file_size = 0l;
	/** 文件相对地址 */
	private String url = "";

	public String getOriginal_file_name() {
		return original_file_name;
	}

	public void setOriginal_file_name(String original_file_name) {
		this.original_file_name = original_file_name;
	}

	public String getAbsolute_url() {
		return absolute_url;
	}

	public void setAbsolute_url(String absolute_url) {
		this.absolute_url = absolute_url;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Long getFile_size() {
		return file_size;
	}

	public void setFile_size(Long file_size) {
		this.file_size = file_size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "FtpInfo [original_file_name=" + original_file_name + ", absolute_url=" + absolute_url + ", _id=" + _id
				+ ", file_size=" + file_size + ", url=" + url + "]";
	}

}
