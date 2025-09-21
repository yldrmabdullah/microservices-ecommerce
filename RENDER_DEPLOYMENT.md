# 🚀 Render ile Deployment Rehberi

## 📋 Gereksinimler
- GitHub hesabı
- Render hesabı (ücretsiz)
- Projeniz GitHub'da

## 🔧 Adım Adım Kurulum

### 1. Render'e Giriş
1. [Render.com](https://render.com) adresine gidin
2. "Get Started for Free" butonuna tıklayın
3. GitHub hesabınızla giriş yapın

### 2. PostgreSQL Database Oluşturma
1. "New +" → "PostgreSQL" seçin
2. Name: `ecommerce-db`
3. Database: `productdb` (veya istediğiniz isim)
4. User: `postgres`
5. Password: Güçlü bir şifre oluşturun
6. "Create Database" butonuna tıklayın
7. Connection string'i kopyalayın

### 3. Servisleri Ekleme

#### A. Product Service
1. "New +" → "Web Service" seçin
2. GitHub repo'nuzu seçin
3. Ayarlar:
   - **Name**: `product-service`
   - **Root Directory**: `services/product-service`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/product-service-1.0.0.jar`
   - **Environment**: `Java`
4. Environment Variables:
   - `SPRING_PROFILES_ACTIVE=prod`
   - `SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/productdb`
   - `SPRING_DATASOURCE_USERNAME=postgres`
   - `SPRING_DATASOURCE_PASSWORD=your-password`
5. "Create Web Service" butonuna tıklayın

#### B. Order Service
1. "New +" → "Web Service" seçin
2. GitHub repo'nuzu seçin
3. Ayarlar:
   - **Name**: `order-service`
   - **Root Directory**: `services/order-service`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/order-service-1.0.0.jar`
   - **Environment**: `Java`
4. Environment Variables:
   - `SPRING_PROFILES_ACTIVE=prod`
   - `SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/orderdb`
   - `SPRING_DATASOURCE_USERNAME=postgres`
   - `SPRING_DATASOURCE_PASSWORD=your-password`
5. "Create Web Service" butonuna tıklayın

#### C. Gateway Service
1. "New +" → "Web Service" seçin
2. GitHub repo'nuzu seçin
3. Ayarlar:
   - **Name**: `api-gateway`
   - **Root Directory**: `gateway`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/gateway-1.0.0.jar`
   - **Environment**: `Java`
4. Environment Variables:
   - `SPRING_PROFILES_ACTIVE=prod`
5. "Create Web Service" butonuna tıklayın

#### D. UI Service
1. "New +" → "Web Service" seçin
2. GitHub repo'nuzu seçin
3. Ayarlar:
   - **Name**: `ecommerce-ui`
   - **Root Directory**: `ui`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/ui-1.0.0.jar`
   - **Environment**: `Java`
4. Environment Variables:
   - `SPRING_PROFILES_ACTIVE=prod`
5. "Create Web Service" butonuna tıklayın

### 4. Servis Bağlantıları
1. Her servis deploy olduktan sonra URL'lerini not edin
2. Gateway servisinde environment variables güncelleyin:
   - `PRODUCT_SERVICE_URL=https://your-product-service.onrender.com`
   - `ORDER_SERVICE_URL=https://your-order-service.onrender.com`
3. UI servisinde environment variables güncelleyin:
   - `GATEWAY_URL=https://your-gateway.onrender.com`

## 🌐 Erişim
- Ana uygulama: UI servisinin URL'i
- API Gateway: Gateway servisinin URL'i
- Product API: `{gateway-url}/api/products`
- Order API: `{gateway-url}/api/orders`

## 💰 Maliyet
- Ücretsiz tier: 750 saat/ay
- Bu proje için yeterli
- Sleep mode: 15 dakika inaktivite sonrası

## 🔧 Sorun Giderme
1. **Build Hatası**: Maven dependencies kontrol edin
2. **Database Bağlantı Hatası**: Connection string'i kontrol edin
3. **Servis Bulunamadı**: Environment variables'ları kontrol edin
4. **Sleep Mode**: İlk istek yavaş olabilir (cold start)

## 📞 Destek
- Render Discord: https://discord.gg/render
- Dokümantasyon: https://render.com/docs