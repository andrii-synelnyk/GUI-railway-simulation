package railroad.entities;

import java.util.ArrayList;

public class Station {

    private static int uniqueId = 0;
    private int id;
    private ArrayList<Connection> connections;

    public Station() {
        this.id = uniqueId++;
        this.connections = new ArrayList<>();
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public ArrayList<Connection> getConnections(){
        return connections;
    }

    @Override
    public String toString(){
        return "Station(" + id + ")";
    }

    public boolean hasConnectionTo(Station station){
        for (Connection connection : connections) {
            if (connection.getStation1().equals(station) || connection.getStation2().equals(station)) {
                return true;
            }
        }
        return false;
    }

    public void removeSpecificConnection(Connection connection){
        connections.remove(connection);
    }

    public int getId(){
        return id;
    }
}