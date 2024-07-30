burada kullanılan dataprovider, excel dosyasındaki başlık hariç ürünleri ve bulundukları satırları
tek tek gönderir. eğer ürün satırında boşluk varsa o satırı atlar.



!a ile başlayan classlar --> Base classlardır, her b classta olması gerekmektedir.
!b ile başlayan classlar --> Base class sonrasında çalıştırılan farklı seçenekleri sunan classlardır.
iki b aynı anda çalışabilir ama çakışmalar yaşanabilir, kontrol edilmeden çalıştırılmamalıdır.
!c ile başlayan classlar --> Base class üzerine eklenen b class özelliklerini işleten ortak classlardır. 
Aslında base classtır ama içinde çalıştırılan classa göre farklı sonuçlar verir.
<<<<<<< HEAD


