# 🧩 Personel Yönetim Sistemi (Microservices Architecture)

Personel Yönetim Sistemi; bir işletmenin **personel kayıtlarını, çalışma saatlerini, izinlerini, performans değerlendirmelerini ve maaş süreçlerini** yönetmek için geliştirilmiş **mikroservis tabanlı bir web uygulamasıdır**.

Proje, **Spring Boot**, **Spring Cloud**, **JWT**, **RabbitMQ** ve **Event-Driven Architecture** kullanılarak gerçek kurumsal sistemleri baz alacak şekilde tasarlanmıştır.

---

## 🚀 Proje Amaçları

- İnsan kaynakları süreçlerini dijitalleştirmek
- Ölçeklenebilir ve bağımsız mikroservis mimarisi kurmak
- Servisler arası bağımlılığı azaltmak
- Asenkron mesajlaşma ile sistem performansını artırmak
- Gerçek dünya enterprise mimarisini simüle etmek

---

## 🏗️ Mimari Genel Bakış

Proje **Spring Boot Microservices Architecture** ile geliştirilmiştir.

### Kullanılan Teknolojiler

- Java 17
- Spring Boot
- Spring Cloud (Eureka, Config Server, Gateway)
- Spring Security (JWT)
- Spring Data JPA
- RabbitMQ
- MySQL
- Maven
- Docker 

---

## 🔧 Mikroservis Listesi (Toplam 8 Servis)

| Servis | Açıklama |
|------|---------|
| **eureka-server** | Service Discovery |
| **config-server** | Merkezi konfigürasyon yönetimi |
| **api-gateway** | API giriş noktası |
| **auth-service** | Kullanıcı kayıt, giriş, JWT |
| **employee-service** | Personel CRUD işlemleri |
| **performance-service** | Çalışma saatleri, izinler ve performans |
| **salary-service** | Maaş ve ödeme işlemleri |
| **email-service** | Asenkron mail gönderimi |

---

## 🔐 Kimlik Doğrulama & Yetkilendirme

- JWT token üretilir
- Rol bazlı erişim kontrolü uygulanır:
  - ADMIN
  - MANAGER
  - EMPLOYEE
- API Gateway üzerinden güvenli erişim sağlanır

---

## 👤 Personel Yönetimi

- Personel ekleme, silme, güncelleme
- Departman ve pozisyon yönetimi
- İletişim bilgileri
- Yetkilendirme kontrolü

---

## ⏱️ Çalışma Saatleri, İzinler & Performans

**Performance-Service** aşağıdaki işlemleri kapsar:

- Çalışma saatlerinin kaydedilmesi
- İzin taleplerinin oluşturulması
- Yönetici onay / red mekanizması
- Performans değerlendirme formları
- Personel geri bildirimleri
- Performans kayıtlarının saklanması

---

## 💰 Maaş & Ödeme Yönetimi

- Personel maaş bilgileri
- Ödeme kayıtları
- Maaş geçmişi
- Maaş ödendiğinde otomatik mail bildirimi

---

## 📬 Mail Sistemi (Event Driven Architecture)

Mail sistemi **tamamen asenkron** çalışır ve **RabbitMQ** kullanır.

### Mail Akış Senaryoları

#### 1️⃣ Şifre Sıfırlama
 Auth-Service → RabbitMQ → Email-Service → Mail gönderimi

#### 2️⃣ Yeni Personel Eklendi
 Employee-Service → RabbitMQ → Email-Service → Hoşgeldin maili

#### 3️⃣ Maaş Ödemesi
 Salary-Service → RabbitMQ → Email-Service → Maaş bildirimi

 Email-Service sadece **consumer**, diğer servisler **producer** rolündedir.

---

## 🐇 RabbitMQ Kullanımı

### Producer Servisler
- auth-service
- employee-service
- salary-service
- performance-service

### Consumer Servis
- email-service

📌 Servisler arası **direct dependency yoktur**, event-driven yapı kullanılmıştır.

---

## ▶️ Projeyi Çalıştırma

```bash
# Eureka Server
cd eureka-server
mvn spring-boot:run

# Config Server
cd config-server
mvn spring-boot:run

# Diğer servisler
mvn spring-boot:run











