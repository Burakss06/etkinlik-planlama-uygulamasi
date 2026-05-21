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
- **Hata Yönetimi:** Global Exception Handling (ResponseEntity ile merkezi hata yönetimi)
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
`EtkinlikPlanlama_Backend` klasörüne gidin. Projeyi derlemek, testleri çalıştırmak veya projeyi ayağa kaldırmak için sisteminizdeki kurulu Maven'ı (`mvn`) veya projenin içindeki Maven Wrapper'ı (`mvnw` / `mvnw.cmd`) kullanabilirsiniz:

#### A Seçeneği (Kurulu Maven ile):
1. Projeyi derleyin ve testleri çalıştırın:
   ```bash
   mvn clean test
   ```
2. Uygulamayı başlatın:
   ```bash
   mvn spring-boot:run
   ```

#### B Seçeneği (Maven Wrapper ile - Maven kurulu değilse):
1. Projeyi derleyin ve testleri çalıştırın:
   - Windows için:
     ```cmd
     .\mvnw.cmd clean test
     ```
   - macOS / Linux için:
     ```bash
     ./mvnw clean test
     ```
2. Uygulamayı başlatın:
   - Windows için:
     ```cmd
     .\mvnw.cmd spring-boot:run
     ```
   - macOS / Linux için:
     ```bash
     ./mvnw spring-boot:run
     ```

*Uygulama varsayılan olarak **8090** portunda çalışacaktır.*

### 2. Frontend Projesini Başlatma
`EtkinlikPlanlama_Frontend` klasörüne gidin. 

1. Gerekli bağımlılıkları (paketleri) yüklemek için:
   ```bash
   npm install
   ```
2. Uygulamayı yerel sunucuda çalıştırmak için aşağıdaki komutlardan birini tercih edebilirsiniz:
   - **Seçenek 1 (Paket Yöneticisi ile):**
     ```bash
     npm start
     ```
   - **Seçenek 2 (Doğrudan Angular CLI ile - Sistemde global Angular CLI kurulu ise):**
     ```bash
     ng serve
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
- Tüm hata durumları `GlobalException` sınıfı ile merkezi olarak yönetilmekte ve `ResponseEntity` durum kodları ile dönmektedir.
- Etkinlik sorgulama uç noktalarında Spring Cache entegrasyonu aktif edilmiştir.
