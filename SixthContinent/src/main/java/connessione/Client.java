package connessione;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Client {
	CloseableHttpClient httpclient = HttpClientBuilder.create().build();
	HttpResponse response;
	private BasicResponseHandler handler = new BasicResponseHandler();
	private List<User> credentialsList = new ArrayList<User>();
	private String token, user_id, username, password;

		public void getAccessOptions() throws IOException {
		User user1 = new User("inserisci email","inserisci password");
		User user2 = new User("inserisci email","inserisci password");
		User utente = new User();
		credentialsList = utente.creaLista(user1, user2);

				
			for(User oggUtente : credentialsList) { //invio al ciclo for la lista credentialsList e gli dico:
				//per ogni Oggetti di tipo User contenuto nella lista chiamalo oggUtente e esegui il codice che ho scritto tra parentesi
				
				username = oggUtente.getUsername();
				password = oggUtente.getPassword();
				
				HttpOptions httpOptions = new HttpOptions("https://prod.sixthcontinent.com/webapi/getaccesstoken");
				response = httpclient.execute(httpOptions);
				if(response.getStatusLine().getStatusCode() == 204) {
					System.out.println("options andato");
					getAccessTokenPost();
				}
				
			}

			
		}

	

	public void getAccessTokenPost() throws IOException {
		HttpPost httPost = new HttpPost("https://prod.sixthcontinent.com/webapi/getaccesstoken");
		httPost.addHeader("content-type", "application/x-www-form-urlencoded");
		String payloadString = "reqObj={\"client_id\":\"1_3ofdwe6u02kgg4ock4os4okc4ss4gckc80ccw000kkc8wo4gsc\",\"client_secret\":\"4mjnllttzpycgss4og8koc40gk8ocskko8kc4888c08wkc4s8g\",\"grant_type\":\"password\",\"username\":\""+username+"\",\"password\":\""+password+"\"}";
		StringEntity xmlEntity = new StringEntity(payloadString);
		httPost.setEntity(xmlEntity);


		response = httpclient.execute(httPost);
		String body = handler.handleResponse(response);
		JsonObject obj = new JsonParser().parse(body).getAsJsonObject();
		JsonObject obj1 = obj.getAsJsonObject("data");
		token = obj1.get("access_token").getAsString();

		if(response.getStatusLine().getStatusCode() == 200) {
			System.out.println("post andato");
			getKaptcha();

		}

	}

	private void getKaptcha() throws IOException {
		HttpGet httpGet = new HttpGet("https://ssl.kaptcha.com/collect/sdk?m=171392&s=514492909642e311cbabaa47d9c96943");
		response = httpclient.execute(httpGet);
		if(response.getStatusLine().getStatusCode() == 200) {
			System.out.println("kapcha andato");
			getLoginOption();

		}


	}

	private void getLoginOption() throws IOException{
		HttpOptions httpOptions = new HttpOptions("https://prod.sixthcontinent.com/api/logins?access_token="+token);
		response = httpclient.execute(httpOptions);
		if(response.getStatusLine().getStatusCode() == 204) {
			System.out.println("login options effettuato");
			getLoginPost();
		}

	}

	private void getLoginPost() throws IOException {
		HttpPost httPost = new HttpPost("https://prod.sixthcontinent.com/api/logins?access_token="+token);
		httPost.addHeader("content-type", "application/x-www-form-urlencoded");
		String payloadCredentials = "reqObj={\"username\":\""+username+"\",\"password\":\""+password+"\"}";
		StringEntity xmlEntity = new StringEntity(payloadCredentials);
		httPost.setEntity(xmlEntity);

		response = httpclient.execute(httPost);
		String credentials = handler.handleResponse(response);
		JsonObject obj = new JsonParser().parse(credentials).getAsJsonObject();
		JsonObject obj1 = obj.getAsJsonObject("data");
		user_id = obj1.get("id").getAsString();
		username = obj1.get("username").getAsString();

		if(response.getStatusLine().getStatusCode() == 200) {
			System.out.println("login effettuato");
			getWalletpost();

		}
	}

	private void getWalletpost() throws IOException {
		HttpPost httPost = new HttpPost("https://prod.sixthcontinent.com/api/getcitizenwalletincome?access_token="+token+"&session_id="+user_id);
		httPost.addHeader("content-type", "application/x-www-form-urlencoded");
		String payloadWallet = "reqObj={\"buyer_id\":"+user_id+"}";
		StringEntity xmlEntity = new StringEntity(payloadWallet);
		httPost.setEntity(xmlEntity);

		response = httpclient.execute(httPost);
		String wallet = handler.handleResponse(response);
		JsonObject obj = new JsonParser().parse(wallet).getAsJsonObject();
		JsonObject obj1 = obj.getAsJsonObject("response");
		JsonObject obj2 = obj1.getAsJsonObject("result");
		String today_gain = obj2.get("today_gain").getAsString();
		String citizen_income_available = obj2.get("citizen_income_available").getAsString();
		String currency_symbol = obj2.get("currency_symbol").getAsString();
		String points_available = obj2.get("points_available").getAsString();

		if(response.getStatusLine().getStatusCode() == 200) {
			System.out.println(username+" login effettuato correttamente,\n oggi hai guadagnato: "+today_gain+" per un totale di: "+citizen_income_available+""+currency_symbol+" e: "+points_available+" punti momosy.");


		}

	}


}
