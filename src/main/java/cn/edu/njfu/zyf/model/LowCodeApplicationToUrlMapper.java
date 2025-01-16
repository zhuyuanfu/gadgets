package cn.edu.njfu.zyf.model;

public class LowCodeApplicationToUrlMapper {
	private String lowCodePlatformApplicationName;
	private String queryNumberOfSignInsUrl;
	private String downloadUrl;
	private String pictureFileName;
	
	public LowCodeApplicationToUrlMapper(String lowCodePlatformApplicationName, String queryNumberOfSignInsUrl,
			String downloadUrl, String pictureFileName) {
		super();
		this.lowCodePlatformApplicationName = lowCodePlatformApplicationName;
		this.queryNumberOfSignInsUrl = queryNumberOfSignInsUrl;
		this.downloadUrl = downloadUrl;
		this.pictureFileName = pictureFileName;
	}

	public String getPictureFileName() {
		return pictureFileName;
	}

	public void setPictureFileName(String pictureFileName) {
		this.pictureFileName = pictureFileName;
	}

	public String getLowCodePlatformApplicationName() {
		return lowCodePlatformApplicationName;
	}

	public void setLowCodePlatformApplicationName(String lowCodePlatformApplicationName) {
		this.lowCodePlatformApplicationName = lowCodePlatformApplicationName;
	}



	public String getQueryNumberOfSignInsUrl() {
		return queryNumberOfSignInsUrl;
	}

	public void setQueryNumberOfSignInsUrl(String queryNumberOfSignInsUrl) {
		this.queryNumberOfSignInsUrl = queryNumberOfSignInsUrl;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
}
