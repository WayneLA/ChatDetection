package Tables;

public class Group {
	private Integer id;
	private String sid;
	private String name;
	private Integer auto_upload;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String group) {
		this.sid = group;
	}
	public Integer getAuto_upload() {
		return auto_upload;
	}
	public void setAuto_upload(Integer auto_upload) {
		this.auto_upload = auto_upload;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
