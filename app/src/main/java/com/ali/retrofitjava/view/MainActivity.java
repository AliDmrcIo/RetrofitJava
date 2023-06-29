package com.ali.retrofitjava.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ali.retrofitjava.R;
import com.ali.retrofitjava.adapter.RecyclerViewAdapter;
import com.ali.retrofitjava.model.CryptoModel;
import com.ali.retrofitjava.service.CryptoAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    ArrayList<CryptoModel> cryptoModels; //gelecek verileri çekmek ve bir arrayliste aktarmak için oluşturduk bunu
    private String BASE_URL="https://raw.githubusercontent.com/";
    Retrofit retrofit;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    CompositeDisposable compositeDisposable; //bu calları vs herşeyi bir süre sonra kullanıldıktan sonra siliyor bununla birlikte kullanıldığı zaman callar. Örneğin bu aktiviteden başka bir yere gidersek veya app kapanırsa vs gibi durumlarda yapıyor bu işlemi. Hafızayı daha verimli kullanmak için yani bu. Bunu da retrofitle direkt kullanamıyoruz, o yüzden RxJava bize bu konuda yardımcı oluyor. Tek kullanımlık, bir defaya mahus, çöpe atılabilir anlamlarına geliyor zaten disposable. Bunun sayesinden birden fazla kullan at objesini aynı yere koyup sonrasında tek seferde temizleyebiliyoruz hafızadan. Bunu kullanmaya karar verdkten sonra artık callları silip observable yazıyoruz, o call yerine geçecek

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json

        recyclerView=findViewById(R.id.recyclerView);

        //Retrofit & Json
        Gson gson=new GsonBuilder().setLenient().create();//burada Json'ı aşağıda addConverter'da kullanmak için oluşturuyor

        retrofit=new Retrofit.Builder().baseUrl(BASE_URL)//retrofit objemizi oluşturduk asyncronious şekilde verileri çekmek için
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//compositeDesposible olayından sonra buraya gelip RxJava kullanacağız dememiz gerekiyor ve bunun yöntemi de budur
                .addConverterFactory(GsonConverterFactory.create(gson))//verilerin json formatında geleceğini söyledik
                .build();

        loadData();


    }

    private void loadData(){//verileri almak için burayı kullanacağız

        final CryptoAPI cryptoAPI=retrofit.create(CryptoAPI.class); //Oluşturduğumuz Interface'imizi burada(veriyi çekeceğimiz yerde) çağırmamız lazımmış
        //veriyi çekmek için Interface'imizin içerisinde belirtiğimiz gibi Call methoduyla çekeceğiz

        compositeDisposable=new CompositeDisposable();
        compositeDisposable.add(cryptoAPI.getData()
                .subscribeOn(Schedulers.io())//hangi thread'de bu gözlemleme/kayıt olma işleminin yapılacağını yazıyoruz burada. Bu gözlemlenebilir obje dediğimiz şey de CryptıAPI interface'inde bulunan Observable<List<CryptoModel>> getData(); satırının hangi thread de gözlemleneceği
                .observeOn(AndroidSchedulers.mainThread())//subscribeOn'da yazılanları nerede göstereceğimizi yazıoz. Ora da mainthread
                .subscribe(this::handleResponse)//bütün bu işlemler sonucunda ortaya çıkan şeyi nerede ele alacağız bunu söylicez
                //tüm calları yazmak yerine aha şu 3 satırla tüm işi hallettik
        );


        /*

        Burada desposible kullanacağımızdan dolayı burayı komple değiştircem ve disposible ile tekrar yazacağımdan dolayı yorum satırı haline getirdim

        Call<List<CryptoModel>> call = cryptoAPI.getData();

        call.enqueue(new Callback<List<CryptoModel>>() {//asyncronius şekilde bunu yapabileceğimizi söylüyor bize enqueue ve içerisine new Call yazınca aşağıdaki default methodları getiriyor. Buradaki call sayısı arttıkça işler karışmaya başlıyor, aynı API üzerinden birsürü farklı call yapmaya başlarak sıkıntılar yaşarız. RxJava bunları daha iyi yönetmek ve kodlarımızı daha güzel yönetmek için burada var
            @Override
            public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) { //veriyi çekerken cevap geldiyse

                if(response.isSuccessful()){//eğer gelen cevap başarılıysa
                    List<CryptoModel> responseList=response.body(); //bu bize crypto model listesi veriyor
                    cryptoModels=new ArrayList<>(responseList);//burada verilen listeyi arraylist'e çevirdim

                    //RecyclerView işlemleri
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));//recylcerview'da bu söylediğimiz rowlar nasıl gösterilecek, yanyana mı, alt alta mı onu söyleyeceğiz
                    recyclerViewAdapter=new RecyclerViewAdapter(cryptoModels); //burada bizden bir arraylist istedi ve biz de hemen yukarıda hallettiğimiz cryptomodels arraylist'ini verdik
                    recyclerView.setAdapter(recyclerViewAdapter);

                    //burayı yaptıktan sonra gidip manifestte     <uses-permission android:name="android.permission.INTERNET"></uses-permission>   satırını eklememiz gerekiyor çünkü interneti kullancağımız her konuda izin istemeliyiz

                   for(CryptoModel cryptoModel:cryptoModels){
                        System.out.println(cryptoModel.price);
                    }

                }


            }

            @Override
            public void onFailure(Call<List<CryptoModel>> call, Throwable t) {//veriyi çekerken eğer bir hata olursa, cevap gelmediyse ne yapayım demek bu
                    t.printStackTrace();
            }
        });
*/
    }

    private void handleResponse(List<CryptoModel> cryptoModelList){//yukarıda subscribe içerisine yazmak için bu methodu yazdık. loadData methodu içerisindeki şeyleri buraya yazcam

        cryptoModels=new ArrayList<>(cryptoModelList);//burada verilen listeyi arraylist'e çevirdim

        //RecyclerView işlemleri
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));//recylcerview'da bu söylediğimiz rowlar nasıl gösterilecek, yanyana mı, alt alta mı onu söyleyeceğiz
        recyclerViewAdapter=new RecyclerViewAdapter(cryptoModels); //burada bizden bir arraylist istedi ve biz de hemen yukarıda hallettiğimiz cryptomodels arraylist'ini verdik
        recyclerView.setAdapter(recyclerViewAdapter);


    }

    @Override
    protected void onDestroy() {//aktivite kapandıktan sonra desposible ile verilerin silineceği kısım
        super.onDestroy();
        compositeDisposable.clear();
    }
}