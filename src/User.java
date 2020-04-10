public class Peer
{
    //скорее клиент в моем случае
    String name;
    String address;
    Peer()
    {
        name = "";
        address = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
