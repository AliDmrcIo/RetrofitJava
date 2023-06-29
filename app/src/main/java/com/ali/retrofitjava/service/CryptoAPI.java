package com.ali.retrofitjava.service;
//https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json

import com.ali.retrofitjava.model.CryptoModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CryptoAPI {
    //Retrofitle çalışırken herşeyden önce servisini oluşturacağımız bir Interface yazmamız lazım. Bu da orası
    //Burada GET, POST, UPDATE, DELETE işlemleri yapılır
    //Get daha çok veriyi almak için, post ise daha çok sunucuya veriyi yazmak için kullanılır

    @GET("atilsamancioglu/K21-JSONDataSet/master/crypto.json")//burada www.websiteadi.com/price?=key gibi bir adresten alacağımız şey price ise direkt / 'dan sonrasını almalıyız. Base Url'i almıyoruz. Bu adres aslında currency ve price değerlerinin olduğu site. Buradan sonra işlem bitmiyor bize hala hata veriyor, bunun altına bu GET'i hangi methodla yapacağımızı söylememiz gerekiyor. Bu da call ile olur.
    Observable<List<CryptoModel>> getData(); //Observable gözlemlenebilir demektir. Veri setinde bir değişiklik olduğunda, bu değişikliği bunu gözlemleyen objelere bildirme görevini üstleniyor diyebiliriz. call yerine kullanıyoruz bunu

    //Call<List<CryptoModel>> getData();//Bir önceki satırda bu adresten değerleri al(currency ve price) ve bu satırda da dedik ki; bu alınan değerleri Liste yap ve CryptoModel adıyla yzadığım class'ta ki değerlere eşle.



}
