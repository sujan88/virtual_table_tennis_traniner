package player;

public class PlayerInfo {
private int playeId;
private String  playerName;

private static PlayerInfo instance=null;

 private PlayerInfo(){
	 this.playeId=0;
	 this.playerName="";
 }
public static PlayerInfo getInstance(){
	
		instance=new PlayerInfo();
	
	return instance;
}

public int getPlayeId() {
	return playeId;
}
public void setPlayeId(int playeId) {
	this.playeId = playeId;
}
public String getPlayerName() {
	return playerName;
}
public void setPlayerName(String playerName) {
	this.playerName = playerName;
}

}
