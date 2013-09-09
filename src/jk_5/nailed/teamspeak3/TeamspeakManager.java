package jk_5.nailed.teamspeak3;

import com.beust.jcommander.internal.Lists;
import jk_5.nailed.Nailed;
import jk_5.nailed.teamspeak3.api.JTS3ServerQuery;
import jk_5.nailed.teamspeak3.api.TeamspeakActionListener;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * No description given
 *
 * @author jk-5
 */
public class TeamspeakManager implements TeamspeakActionListener {

    private final JTS3ServerQuery server = new JTS3ServerQuery();

    private boolean enabled = Nailed.config.getTag("teamspeak").getTag("enabled").setComment("Set to false to disable teamspeak integration").getBooleanValue(false);
    private final String host = Nailed.config.getTag("teamspeak").getTag("host").setComment("IP address / host name for the teamspeak server").getValue("localhost");
    private final int port = Nailed.config.getTag("teamspeak").getTag("port").setComment("Port for the teamspeak query interface").getIntValue(10011);
    private final String username = Nailed.config.getTag("teamspeak").getTag("username").setComment("Username for the query interface. Leave empty for none").getValue("");
    private final String password = Nailed.config.getTag("teamspeak").getTag("password").setComment("Password for the query interface. Leave empty for none").getValue("");

    private final List<TeamspeakClient> clients = Lists.newArrayList();

    public void connect() {
        System.out.println("Connecting to teamspeak...");
        if (!this.enabled) return;
        if (!this.server.connectTS3Query(this.host, this.port)) {
            System.out.println("Failed!");
            return;
        }
        this.server.loginTS3(this.username, this.password);
        if (!this.server.selectVirtualServer(1)) {
            this.displayError();
            return;
        }
        this.server.setTeamspeakActionListener(this);

        this.server.addEventNotify(4, 0);
        this.server.addEventNotify(5, 14);
        this.server.addEventNotify(5, 17);
        this.server.addEventNotify(5, 20);

        System.out.println("Connected to teamspeak!");
        this.refreshClientList();
    }

    public void teamspeakActionPerformed(String eventType, HashMap<String, String> eventInfo) {
        this.refreshClientList();
    }

    public void moveClientToChannel(TeamspeakClient client, int channelID) {
        if (server.moveClient(client.getClientID(), channelID, null)) {
            System.out.println("Error while moving " + client.getNickname());
            System.out.println(server.getLastError());
        }
    }

    private void refreshClientList() {
        this.clients.clear();
        Vector<HashMap<String, String>> dataClientList = this.server.getList(JTS3ServerQuery.LISTMODE_CLIENTLIST, "-info,-times");
        if (dataClientList == null) return;
        for (HashMap<String, String> hm : dataClientList) this.clients.add(new TeamspeakClient(hm));
    }

    public TeamspeakClient getClientForUser(String username) {
        for (TeamspeakClient client : this.clients) {
            if (client.getNickname().equals(username)) {
                return client;
            }
        }
        return null;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getNicknames() {
        List<String> names = Lists.newArrayList();
        for (TeamspeakClient client : this.clients) {
            names.add(client.getNickname());
        }
        return names;
    }

    private void displayError() {
        String error = this.server.getLastError();
        if (error != null) {
            System.out.println("Teamspeak error:");
            System.out.println(error);
            if (this.server.getLastErrorPermissionID() != -1) {
                HashMap<String, String> permInfo = this.server.getPermissionInfo(this.server.getLastErrorPermissionID());
                if (permInfo != null) {
                    System.out.println("Missing Permission: " + permInfo.get("permname"));
                }
            }
        }
    }

    /*private void displayHashMap(HashMap<String, String> hm){
        if (hm == null) return;

        Collection<String> cValue = hm.values();
        Collection<String> cKey = hm.keySet();
        Iterator<String> itrValue = cValue.iterator();
        Iterator<String> itrKey = cKey.iterator();

        while (itrValue.hasNext() && itrKey.hasNext()) System.out.println(itrKey.next() + ": " + itrValue.next());
    }*/
}
