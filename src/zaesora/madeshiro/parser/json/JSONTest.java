package zaesora.madeshiro.parser.json;


import java.io.IOException;
import java.io.StringWriter;

/**
 * Class of jMdParser in package zaesora.madeshiro.parser.json
 *
 * @author MađeShirő ƵÆsora
 * @since NOTHING
 */
public class JSONTest {

    public static void main(String[] args) {
        // long i = System.currentTimeMillis();
        // File file = new File("myFyle.json");
        try {
            String jFile =
                    "[\n" +
                            "  {\n" +
                            "    \"_id\": \"5797a2313bf80e3ccad0c248\",\n" +
                            "    \"index\": 0,\n" +
                            "    \"guid\": \"64b5112d-3ab8-4905-ad6a-f220aa642913\",\n" +
                            "    \"isActive\": true,\n" +
                            "    \"balance\": \"$2,906.28\",\n" +
                            "    \"picture\": \"http://placehold.it/32x32\",\n" +
                            "    \"age\": 23,\n" +
                            "    \"eyeColor\": \"brown\",\n" +
                            "    \"name\": \"Rosetta Reynolds\",\n" +
                            "    \"gender\": \"female\",\n" +
                            "    \"company\": \"EARTHWAX\",\n" +
                            "    \"email\": \"rosettareynolds@earthwax.com\",\n" +
                            "    \"phone\": \"+1 (841) 400-2186\",\n" +
                            "    \"address\": \"108 Bedford Avenue, Grandview, Rhode Island, 7998\",\n" +
                            "    \"about\": \"Sit elit commodo eiusmod nisi nisi nisi ullamco labore ipsum non. Aliquip exercitation esse aute non quis non. Pariatur nisi sint eiusmod cupidatat ullamco ea excepteur laborum est aliquip. Eu pariatur cillum id sunt duis aliquip incididunt exercitation quis ipsum anim. Ullamco eu laboris elit fugiat nulla ipsum occaecat sit. Nisi reprehenderit officia reprehenderit adipisicing ut aliquip et. Non quis sint culpa voluptate qui excepteur consectetur.\\r\\n\",\n" +
                            "    \"registered\": \"2014-07-18T05:30:49 -02:00\",\n" +
                            "    \"latitude\": -57.797661,\n" +
                            "    \"longitude\": 138.878182,\n" +
                            "    \"tags\": [\n" +
                            "      \"magna\",\n" +
                            "      \"ipsum\",\n" +
                            "      \"sint\",\n" +
                            "      \"sunt\",\n" +
                            "      \"Lorem\",\n" +
                            "      \"in\",\n" +
                            "      \"mollit\"\n" +
                            "    ],\n" +
                            "    \"friends\": [\n" +
                            "      {\n" +
                            "        \"id\": 0,\n" +
                            "        \"name\": \"Deborah Freeman\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"id\": 1,\n" +
                            "        \"name\": \"Small Schwartz\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"id\": 2,\n" +
                            "        \"name\": \"Margie Mathews\"\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"greeting\": \"Hello, Rosetta Reynolds! You have 9 unread messages.\",\n" +
                            "    \"favoriteFruit\": -10e2\n" +
                            "  }\n" +
                            "]";

            //if(!file.exists())
            //    file.createNewFile();
            //FileWriter stringWriter = new FileWriter(file);
            long average = 0,i;
            for(int a = 0; a < 100; a ++) {
                i = System.currentTimeMillis();
                JSONArray jsonArray = new JSONParser().parse(
                        jFile
                );
                JSONObject obj = (JSONObject) jsonArray.get(0);
                //noinspection ConstantConditions
                if((a == 9) || (a == 162) || (a == 5000)) {
//                    System.out.println(obj.get("about"));

                    StringWriter writer = new StringWriter();
                    jsonArray.writeForFile(writer);
                    System.out.println(writer.toString() + "\n-> ");
                    Object[] path = {0, "isActive"};
                    System.out.println(jsonArray.getObject(path));
                }
                //jsonArray.writeForFile(stringWriter);
                //stringWriter.flush();
                //stringWriter.close();
                //if(a < 99) {
                //    stringWriter = new FileWriter(file);
                //}

                average += (System.currentTimeMillis()-i);
            }

            //stringWriter.close();

            System.out.println("avg = " + ((double)average/100.0));

        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(System.currentTimeMillis() - i); // get time in millis

    }

}
