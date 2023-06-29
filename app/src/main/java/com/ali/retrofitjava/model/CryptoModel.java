package com.ali.retrofitjava.model;

import com.google.gson.annotations.SerializedName;

public class CryptoModel {//bu class'ın oluşturulma amacı modeli oluşturmaktır
    //çekeceğimiz API'de 2 tane değer var ve onlar da currency ve price. Bu yüzden bunları almak için bir class oluşturduk ve bu class'a da model deniyor bu amaçla yapıldığından

    @SerializedName("currency")//bunu yaparak gelecek datayı direkt okuyabiliyorum ancak bu serializedName'in eşleşeceği veri ile olan değişkenin hemen üzerinde olması gerekli
    public String currency;

    @SerializedName("price")
    public String price;

}
