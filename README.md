# Etkinlik Planlama Uygulaması

Bu proje, kullanıcıların etkinlik oluşturabildiği, diğer kullanıcıların etkinliklerini görüntüleyip katılım sağlayabildiği ve kendi etkinliklerini yönetebildiği kapsamlı bir web uygulamasıdır.

## Proje Genel Bilgileri
- **Başlangıç Tarihi:** 8 Mayıs 2026
- **Teslim Tarihi:** 22 Mayıs 2026

## Kullanılan Teknolojiler

### Backend
- **Dil / Sürüm:** Java 17
- **Çatı:** Spring Boot 4.0.4
- **Veri Tabanı Erişimi:** Spring Data JPA
- **Doğrulama:** Spring Validation
- **Veri Transfer Nesneleri:** DTO Yapısı & ModelMapper
- **Belgelendirme:** Swagger (OpenAPI)
- **Hata Yönetimi:** ResponseEntity Durum Kodları (Hocanın güncel şablonuna göre uyarlanmış Global Exception Handling)
- **Kimlik Doğrulama:** HTTP Session Tabanlı Authentication
- **Önbellekleme:** Spring Cache (Cacheable ve CacheEvict)

### Frontend
- **Framework:** Angular 21.2.0
- **Stil Kütüphanesi:** Bootstrap

### Veritabanı
- H2 Database (Dosya tabanlı / File-based)

---

## Kurulum ve Çalıştırma Adımları

### 1. Backend Projesini Başlatma
1. `EtkinlikPlanlama_Backend` klasörüne gidin.
2. Aşağıdaki komut ile projeyi derleyin ve testleri çalıştırın:
   ```bash
   mvn clean test
   ```
3. Uygulamayı başlatmak için:
   ```bash
   mvn spring-boot:run
   ```
   *Uygulama varsayılan olarak **8090** portunda çalışacaktır.*

### 2. Frontend Projesini Başlatma
1. `EtkinlikPlanlama_Frontend` klasörüne gidin.
2. Gerekli paketleri kurmak için:
   ```bash
   npm install
   ```
3. Uygulamayı yerel sunucuda çalıştırmak için:
   ```bash
   npm start
   ```
   *Uygulama tarayıcınızda otomatik olarak **http://localhost:4200** adresinde açılacaktır.*

---

## Önemli Bağlantılar ve Bilgiler

### Swagger (API Belgelendirmesi)
Backend uygulaması çalışırken API uç noktalarını (endpoints) test etmek ve görüntülemek için aşağıdaki adresi tarayıcınızda açabilirsiniz:
- **Bağlantı Adresi:** [http://localhost:8090/swagger-ui/index.html](http://localhost:8090/swagger-ui/index.html)

### H2 Veritabanı Konsolu
Uygulama çalışırken veritabanı tablolarını ve kayıtlarını incelemek için H2 konsolunu kullanabilirsiniz:
- **Konsol Adresi:** [http://localhost:8090/h2-console](http://localhost:8090/h2-console)
- **JDBC URL:** `jdbc:h2:file:~/etkinlik_planlama_db`
- **User Name:** `sa`
- **Password:** *(Boş bırakınız)*

---

## Proje Kuralları ve Standartları
- Proje katmanlı mimari standartlarına uygun olarak tasarlanmıştır.
- Temiz kod prensipleri gereğince kod tabanında hiçbir yorum satırı bulunmamaktadır.
- Özel exception sınıfları hocanın güncel standartları doğrultusunda kaldırılmış, tüm hata durumları `ResponseEntity` durum kodları ile yönetilmiştir.
- Etkinlik sorgulama uç noktalarında Spring Cache entegrasyonu aktif edilmiştir.
