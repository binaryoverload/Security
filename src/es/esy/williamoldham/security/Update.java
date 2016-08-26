package es.esy.williamoldham.security;

public class Update {

	private String oldVer;
	private String newVer;
	private String url;
	
	public Update(String oldVer, String newVer, String url) {
		this.oldVer = oldVer;
		this.newVer = newVer;
		this.url = url;
	}
	
	public String getOldVer(){
		return oldVer;
	}
	
	public String getNewVer(){
		return newVer;
	}
	
	public String getURL(){
		return url;
	}
	
}
