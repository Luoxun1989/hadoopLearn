/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ZooKeeperTest
 * Author:   admin
 * Date:     2020/6/2 21:29
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author admin
 * @create 2020/6/2
 * @since 1.0.0
 */
public class ZooKeeperTest {
    public static void main(String[] args) {
        try {
//            ls();
//            lsAll();
            testWatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void ls() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("sparkproject1:2181",5000,null);
        List<String> list = zooKeeper.getChildren("/",null);
        for (String s :list) {
            System.out.println(s);
        }
    }
    public static void lsAll() throws Exception {
        ls("/");
    }
    public static void ls(String path) throws Exception {
        System.out.println(path);
        ZooKeeper zooKeeper = new ZooKeeper("sparkproject1:2181",5000,null);
        List<String> list = zooKeeper.getChildren(path,null);
        if (null == list || list.size() < 1){
            return;
        }
        for (String s :list) {
            //先输出孩子
            if(path.equals("/")){
                ls(path + s);
            }
            else{
                ls(path + "/" + s);
            }
        }
    }
    public static void setData(String path) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("sparkproject1:2181",5000,null);
        int version = 0;
        byte[] data = "blogg".getBytes();
        zooKeeper.setData(path,data,version);
    }
    public static void createEmphoral(String path) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("sparkproject1:2181",5000,null);
        int version = 0;
        byte[] data = "blogg".getBytes();
        zooKeeper.create(path,data,ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }
    public static void testWatch() throws Exception {
        final ZooKeeper zooKeeper = new ZooKeeper("sparkproject1:2181",5000,null);
        Stat st = new Stat();
        Watcher watcher = null;
        watcher = new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("数据改了");
                try {
                    zooKeeper.getData("/a",this,null);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        byte[] data = zooKeeper.getData("/a",watcher,st);
        System.out.println(new String(data));
        while (true){
            Thread.sleep(2000);
        }
    }
}