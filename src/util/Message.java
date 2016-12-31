package util;

import java.io.Serializable;

public final class Message implements Serializable{
	
	//��Ϣ�����ߣ� ��Ϣ���ͣ����ӣ�ȡ�����ӵȵȣ�����Ϣ������Ϣ����
	private static final long serialVersionUID = 1L;
	private String name;
    private MessageType type;
    private String msg;
    private int count;

    public Message() {
    	this.name = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setOnlineCount(int count){
        this.count = count;
    }

    public int getOnlineCount(){
        return this.count;
    }
}
