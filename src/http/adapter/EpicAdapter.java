package http.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Epic;

import java.io.IOException;

public class EpicAdapter extends TypeAdapter<Epic> {

    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("type").value("Epic");
        jsonWriter.name("name").value(epic.getName());
        jsonWriter.name("description").value(epic.getDescription());
        jsonWriter.name("id").value(epic.getId());
        jsonWriter.name("status").value(epic.getStatus().toString());
        jsonWriter.name("listOfSubtasks");
        jsonWriter.beginArray();
        epic.getSubtasksOfEpic().stream().forEach(subTaskId -> {
            try {
                jsonWriter.value(String.valueOf(subTaskId));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        jsonWriter.endArray();
        jsonWriter.endObject();
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        JsonElement jsonElement = JsonParser.parseReader(jsonReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Epic epic;
        epic = new Epic(
                jsonObject.get("name").getAsString(),
                jsonObject.get("description").getAsString());
        return epic;
    }
}
