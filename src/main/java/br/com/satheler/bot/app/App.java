package br.com.satheler.bot.app;

import static br.com.satheler.bot.providers.ServiceProvider.AVAILABLE_COMMANDS;
import static br.com.satheler.bot.providers.ServiceProvider.getOnlyNameClass;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import br.com.satheler.bot.app.DataMonitor.DataMonitorListener;
import br.com.satheler.bot.providers.CommandProvider;
import br.com.satheler.bot.providers.ServiceProvider;

/**
 * Application main
 */
public final class App implements Watcher, DataMonitorListener {

    public static App APP;
    public static ServerSocket SERVER_SOCKET;
    public static ServiceProvider SERVICE_PROVIDER;

    private int serverPort;
    private String zNode;
    private DataMonitor dataMonitor;
    private ZooKeeper zookeeper;

    private App(String hostPort, String zNode, int serverPort) throws IOException {
        this.serverPort = serverPort;
        this.zNode = zNode;
        this.zookeeper = new ZooKeeper(hostPort, 3000, this);
        this.dataMonitor = new DataMonitor(this.zookeeper, zNode, null, this);
    }

    public void configureZookeeperNode(String zNode, int serverPort)
            throws KeeperException, InterruptedException, Exception {
        Stat nodeExists = this.zookeeper.exists(zNode, false);
        if (nodeExists != null) {
            throw new Exception("Already zookeeper node exists.");
        }

        String host = this.getMyIP();
        String hostPort = host + ":" + serverPort;

        this.zookeeper.create(zNode, hostPort.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    private String getMyIP() {
        String ip = null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    // *EDIT*
                    if (addr instanceof Inet6Address)
                        continue;

                    ip = addr.getHostAddress();
                    System.out.println(iface.getDisplayName() + " " + ip);
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return ip;
    }

    /**
     * Método para inicializar todo o servidor e a aplicação JAVA.
     * 
     * @param args[] Passagem de argumentos caso necessário à aplicação.
     * @throws IOException Sinaliza que ocorreu uma exceção de I/O de algum tipo de
     *                     falha ou interrupção.
     */
    public static void main(String args[]) {

        if (args.length < 3) {
            System.err.println("USAGE: <serverPort> <zookeeper_host>:<zookeeper_port> <zNode>");
            System.exit(2);
        }

        int port = Integer.parseInt(args[0]);
        String hostPort = args[1];
        String zNode = args[2];

        try {
            APP = new App(hostPort, zNode, port);
            APP.configureZookeeperNode(zNode, port);
            shutdownServerListener();
            executeServer(port);

        } catch (Exception e) {
            // e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            System.exit(3);
        }

    }

    public static void executeServer(int port) throws IOException {
        try {
            SERVICE_PROVIDER = ServiceProvider.getInstance();
            showCommands();
            SERVER_SOCKET = new ServerSocket(port);
            System.out.println("SERVIDOR RODANDO NA PORTA " + port);
            while (true) {
                Socket conexao = SERVER_SOCKET.accept();
                Thread newThread = new Server(conexao);
                newThread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SERVER_SOCKET.close();
        }
    }

    public static void showCommands() {
        System.out.print("Comandos carregados: | ");
        for (CommandProvider command : AVAILABLE_COMMANDS) {
            Class<?> nameClass = command.getClass();
            String commandName = getOnlyNameClass(nameClass);
            System.out.print(commandName + " | ");
        }
        System.out.println();
    }

    public static void shutdownServerListener() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {

                try {
                    Thread.sleep(200);
                    System.out.println("Desligando servidor...");
                    App.APP.finalize();
                } catch (Throwable e) {
                }

            }

        });
    }

    @Override
    public void exists(byte[] data) {
        if (data == null) {
        } else {
            try {
                String zNodeStripped = this.zNode.replaceAll("\\/", "");
                String hostPortStripped = String.valueOf(serverPort);
                FileOutputStream fos = new FileOutputStream(zNodeStripped + "_" + hostPortStripped + ".txt");
                fos.write(data);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // try {
            System.out.println("Starting child");
            // new StreamWriter(child.getInputStream(), System.out);
            // new StreamWriter(child.getErrorStream(), System.err);
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
        }
    }

    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        this.dataMonitor.process(event);
    }

    static class StreamWriter extends Thread {
        OutputStream os;
        InputStream is;

        StreamWriter(InputStream is, OutputStream os) {
            this.is = is;
            this.os = os;
            start();
        }

        public void run() {
            byte b[] = new byte[80];
            int rc;
            try {
                while ((rc = is.read(b)) > 0) {
                    os.write(b, 0, rc);
                }
            } catch (IOException e) {
            }

        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.zookeeper.delete(this.zNode, 0);
    }
}
