//package my.app;
//
//import java.sql.SQLException;
//import java.util.*;
//import java.lang.*;
//
//public class TreeStr {
//    public static HashMap<String, Node> mainFolder = new HashMap<>();
//
//}
//
//class Node{
//    String id, url, type;
//    int size = 0;
//    Node parent = null;
//    ArrayList<Node> children;
//
//    GregorianCalendar curDate;
//    public Node(String id) {
//        this.id = id;
//        children = new ArrayList<>();
//        TreeStr.mainFolder.put(id, this);
//    }
//    public void addChildren(Node child){
//        this.children.add(child);
//    }
//
//    public void addUrl(String url){
//        if(url == "None")
//            return;
//        this.url = url;
//    }
//    public void addType(String type){
//        if(type == "None")
//            return;
//        this.type = type;
//    }
//    public void addSize(String size){
//        if (size == "None" || type.equals("FOLDER"))
//            return;
//        int intSize = Integer.parseInt(size);
//        this.addSumSize(intSize - this.size);
//    }
//
//    public void addSumSize(int size){
//        if (size == 0)
//            return;
//
//        this.size += size;
//        Request.importUpdateItems.add(this);
//        if(parent != null){
//            parent.addSumSize(size);
//        }
//    }
//
//    public void addParent(String parentId) {
//        if (parentId == "None")
//            return;
//
//        if(!TreeStr.mainFolder.containsKey(parentId)){
//            Node parent = new Node(parentId);
//            parent.addType("FOLDER");
//            TreeStr.mainFolder.put(parentId, parent);
//            Request.importNewItems.add(parent);
//        }
//
//        if (this.parent != null && this.parent.id != parentId)
//            changeParent();
//
//        if(!TreeStr.mainFolder.get(parentId).children.contains(this)) {
//            TreeStr.mainFolder.get(parentId).addChildren(this);
//            parent = TreeStr.mainFolder.get(parentId);
//            parent.addSumSize(this.size);
//        }
//    }
//    public void changeParent() {
//        parent.addSumSize(-this.size);
//        this.parent.children.remove(this);
//    }
//    public Map getInfo() {
//        Map result = new HashMap();
//        result.put("type", type);
//        result.put("id", id);
//        result.put("url", url);
//        if (parent != null)
//            result.put("parentId", parent.id);
//        else
//            result.put("parentId", null);
//
//        if (type == "FILE")
//            result.put("children", null);
//        else {
//            JSONArray children_info = new JSONArray();
//            for (Node child: children) {
//                children_info.add(child.getInfo());
//            }
//            result.put("children", children_info);
//        }
//        result.put("size", size);
//        return result;
//    }
//
//    public HashMap<String, String> getAllAttributes(){
//        HashMap<String, String> result = new HashMap<>();
//        result.put("type", type);
//        result.put("id", id);
//        result.put("url", url != null ? url : "null");
//        result.put("size", size + "");
//        result.put("parentId", parent != null ? parent.id : "null");
//
//        StringBuilder children = new StringBuilder("");
//        for(Node child: this.children){
//            children.append(child.id + ", ");
//        }
//        return result;
//    }
//    public void delete()  {
//        for (Node child : children) {
//            child.delete();
//        }
//        TreeStr.mainFolder.remove(id);
//    }
//
//}
//
//class Request{
//    public static HashSet<Node> importNewItems = new HashSet<>();
//    public static HashSet<Node> importUpdateItems = new HashSet<>();
//    public static JSONObject parse(String x){
//        Object obj = JSONValue.parse(x);
//        return (JSONObject) obj;
//    }
//
//    public static void importR(JSONObject x) throws SQLException {
//        String id;
//        Map item;
//        Node file;
//        Map dict_x = (Map) x;
//        Object items_list = dict_x.get("items");
//        for(Object k: (JSONArray) items_list) {
//            item = (Map) k;
//            id = item.get("id").toString();
//
//            if (!TreeStr.mainFolder.containsKey(id)) {
//                file = new Node(id);
//                TreeStr.mainFolder.put(id, file);
//                Request.importNewItems.add(file);
//            } else {
//                file = TreeStr.mainFolder.get(id);
//                Request.importUpdateItems.add(file);
//            }
//
//            file.addUrl(item.getOrDefault("url", "None").toString());
//            file.addType(item.getOrDefault("type", "None").toString());
//            file.addSize(item.getOrDefault("size", "None").toString());
//            file.addParent(item.getOrDefault("parentId", "None").toString());
//            file.curDate = new GregorianCalendar();
//        }
//    }
//
//    public static JSONObject nodesR(String id){
//        Node item = TreeStr.mainFolder.get(id);
//        Map newFolder = item.getInfo();
//        return(new JSONObject(newFolder));
//    }
//    public static void deleteR(String id){
//        Node cur = TreeStr.mainFolder.get(id);
//        cur.delete();
//        if (cur.parent != null)
//            cur.parent.children.remove(cur);
//        if (cur.size != 0){
//            cur.addSumSize(-cur.size);
//        }
//    }
//}
