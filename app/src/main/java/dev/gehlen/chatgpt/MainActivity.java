package dev.gehlen.chatgpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.gehlen.chatgpt.adapter.MessageAdapter;
import dev.gehlen.chatgpt.model.ChatResponse;
import dev.gehlen.chatgpt.model.Message;

import dev.gehlen.chatgpt.service.ChatService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://api.openai.com/v1/"; // Substitua pelo URL correto da API GPT
    private static final String API_KEY = "API_KEY"; // Substitua pelo valor correto da sua chave de API

    private ChatService chatService;

    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerViewMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        chatService = retrofit.create(ChatService.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextMessage = findViewById(R.id.editTextMessage);
                String message = editTextMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage(message);
                }
            }
        });

        // Inicialize a lista de mensagens
        messageList = new ArrayList<>();

        // Inicialize o RecyclerView e o adaptador
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        messageAdapter = new MessageAdapter(messageList);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);
    }

    private void sendMessage(String content) {

        Message userMessage = new Message(content, "user");
        messageList.add(userMessage);
        messageAdapter.notifyItemInserted(messageList.size() - 1);

        // Crie um objeto JSON com a estrutura necessária
        JSONObject messageObject = new JSONObject();
        try {
            messageObject.put("role", "user");
            messageObject.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crie um objeto JSON para incluir o parâmetro 'model'
        JSONObject payloadObject = new JSONObject();
        try {
            payloadObject.put("model", "text-davinci-002");
            payloadObject.put("messages", new JSONArray().put(messageObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Envie a mensagem para a API do chatbot
        Call<ChatResponse> call = chatService.sendMessage(payloadObject.toString());
        // Envie a mensagem para a API do chatbot
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful()) {
                    String botResponse = response.body().getMessage();
                    Message botMessage = new Message(botResponse, "assistant");
                    messageList.add(botMessage);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerViewMessages.smoothScrollToPosition(messageList.size() - 1);
                } else {
                    // Tratar erros na resposta da API do chatbot
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                // Tratar falha na chamada à API do chatbot
            }
        });
    }




}