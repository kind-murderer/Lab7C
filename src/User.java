public class User
{
    //скорее клиент в моем случае
    private String name;
    private String addressOrPassword; //address or password, буду использовать это в зависимости от ситуации
    User(String name, String addressOrPassword)
    {
        this.name = name;
        this.addressOrPassword = addressOrPassword;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return addressOrPassword;
    }
    public String getPassword(){
        return addressOrPassword;
    }
}
