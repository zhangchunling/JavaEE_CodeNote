package com.study.网络编程.UDP协议网络编程.即时通信;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.util.Date;

import javax.activation.MailcapCommandMap;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
/**
 * swing编程：
 * 该类用DefaultListModel来维护用户列表，每个列表项就是一个UserInfo
 * 该类用ImageCellRenderer将列表绘制出用户图标和用户名字
 * 
 * @author Peter
 *
 */
public class LanChat extends JFrame{
	//定义维护用户列表的对象
	private DefaultListModel listModel = new DefaultListModel();
	//定义一个JList对象
	private JList friendsList = new JList(listModel);
	//定义一个用于格式化日期的格式器
	private DateFormat formatter = DateFormat.getDateTimeInstance();
	
	/**
	 * 无参构造
	 */
	public LanChat(){
		super("局域网聊天");
		//设置该JList使用ImageCellRenderer作为单元格绘制器
		friendsList.setCellRenderer(new ImageCellRenderer());
		listModel.addElement(new UserInfo("all", "所有人", null, -2000));
		this.add(new JScrollPane(friendsList));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(2, 2, 160, 600);
	}
	
	/**
	 * 向用户列表添加用户
	 */
	public void addUser(UserInfo user){
		listModel.addElement(user);
	}
	/**
	 * 从用户列表删除用户
	 */
	public void removeUser(int pos){
		listModel.remove(pos);
	}
	
	/**
	 * 根据地址来查询用户
	 */
	public UserInfo getUserBySocketAddress(SocketAddress address){
		for(int i=1;i<this.getUserNum();i++){
			UserInfo user = this.getUser(i);
			if(user.getAddress()!=null&&user.getAddress().equals(address)){
				return user;
			}
		}
		return null;
	}
	
	//--------------下面两个方法是对ListModel的包装
	/**
	 * 获取该聊天容器的用户数量
	 */
	public int getUserNum(){
		return listModel.size();
	}
	/**
	 * 获取指定位置的用户
	 */
	public UserInfo getUser(int pos){
		return (UserInfo) listModel.elementAt(pos);
	}
	
	/**
	 * 内部类：实现JList上的鼠标双击监听器
	 */
	class ChangeMusicLisener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			if(e.getClickCount()>=2){//鼠标点击次数>=2
				//取出鼠标双击时选中的列表项
				UserInfo user = (UserInfo) friendsList.getSelectedValue();
				//如果该列表项对应的用户的交谈窗口为null,创建一个
			/*	if(user.getChatFrame()==null){
					user.setChatFrame(new ChatFrame(null,user));
				}
				//如果用户的窗口没有显示，则该用户的窗口显示出来 
				if(!user.getChatFrame().isShowing()){
					user.getChatFrame().setVisible(true);
				}*/
			}
		}
	}
	
	/**
	 * 处理网络为数据报，该方法将根据聊天信息得到聊天者，并将信息显示在聊天对话中
	 * @param packet 需要处理的数据报
	 * @param single 该信息是否为私聊信息
	 */
	public void processMsg(DatagramPacket packet,boolean single){
		//获取发送该数据报的SocketAddress
		InetSocketAddress srcAddress = (InetSocketAddress)packet.getSocketAddress();
		//如果是私聊信息，则该Packet获取的是DatagramSocket的地址，将端口减1才是对应的MulticastSocket的地址
		if(single){
			srcAddress = new InetSocketAddress(srcAddress.getHostName(),srcAddress.getPort()-1);			
		}
		UserInfo srcUser = this.getUserBySocketAddress(srcAddress);
		if(srcUser!=null){
			//确定消息将要显示到哪个用户对应的窗口上
			UserInfo alertUser = single?srcUser:this.getUser(0);
			//如果该用户对应的窗口为空，显示该窗口
			/*if(alertUser.getChatFrame()==null){
				alertUser.setChatFrame(new ChatFrame(null,alertUser));
			}*/
			//定义添加的提示信息
			String tipMsg = single?"对您说：":"对大家说";
			//显示提示信息
			/*alertUser.getChatFrame().addString(srcUser.getName()+tipMsg+
					formatter.format(new Date()+"\n")+new String(packet.getData(),0,packet.getLength()));
			if(!alertUser.getChatFrame().isShowing()){
				alertUser.getChatFrame().setVisible(true);
			}*/
		}
		
	}
	
	public static void main(String[] args) {
		LanChat lc = new LanChat();
		//new LoginFrame(lc,"请输入用户名、头像后登录");
	}
}
	/**
	 * 定义用于该变JList列表项外观的类
	 */
	class ImageCellRenderer extends JPanel implements ListCellRenderer{

		private ImageIcon icon;
		private String name;
		private Color background;//定义绘制单元格时的背景色
		private Color foreground;//定义绘制单元格时的前景色
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			UserInfo userInfo = (UserInfo)value;
			icon = new ImageIcon("ico/"+userInfo.getIcon()+".gif");
			name = userInfo.getName();
			background = isSelected?list.getSelectionBackground():list.getBackground();
			foreground = isSelected?list.getSelectionForeground():list.getForeground();
			return this;//返回该JPanel对象作为单元格绘制器
		}
		//重写，改变JPanel的外观
		public void paintComponent(Graphics g){
			int imageWidth = icon.getImage().getWidth(null);
			int imageHight = icon.getImage().getHeight(null);
			g.setColor(background);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(foreground);
			//绘制好友图标
			g.drawImage(icon.getImage(), getWidth()/2-imageWidth-2, 10, null);
			g.setFont(new Font("SansSerif",Font.BOLD,18));
			//绘制好友用户名
			g.drawString(name,getWidth()/2-name.length(),imageHight+30);
		}
		//通过该方法来设置ImageCellRenderer的最佳大小
		public Dimension getPreferredSize(){
			return new Dimension(60,80);
		}
		
	}

