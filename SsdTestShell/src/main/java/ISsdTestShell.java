public interface ISsdTestShell {
    void write(String lbs, String data);
    void read(String lbs);
    void fullwrite(String data);
    void fullread();
}
