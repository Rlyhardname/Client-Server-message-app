package Main;

public class EntryPoint {

	EntryPoint() {

	}

	public static void main(String[] args) {

//		DbServer db = new DbServer();
//		db.dropTable();
//		db.createTableMessageData();
//		if(DbServer.createTableChatRoomWarehouse()) {
//			System.out.println("yes");
//		};
		ServerGUI.startGUI();
		ClientLoginGUI.startGUI();
		
		//Test Eror;

		// FOR TESTING MULTIPLE CLIENTS
		// new StartClients();

	}

}
