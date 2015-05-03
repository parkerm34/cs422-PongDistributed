package Controller;

public class ClientMessage {
	private boolean upKey;
	private boolean downKey;
	
	public ClientMessage()
	{
		upKey = false;
		downKey = false;
	}
	
	public ClientMessage(boolean upKey, boolean downKey)
	{
		this.upKey = upKey;
		this.downKey = downKey;
	}
	
	public boolean getUpKey()
	{
		return upKey;
	}
	
	public boolean getDownKey()
	{
		return downKey;
	}
	
	public void setUpKey(boolean upKey)
	{
		this.upKey = upKey;
	}
	
	public void setDownKey(boolean downKey)
	{
		this.downKey = downKey;
	}
}
