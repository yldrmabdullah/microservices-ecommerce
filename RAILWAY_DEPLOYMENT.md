# 🚀 Railway ile Deployment Rehberi

## 📋 Gereksinimler
- GitHub hesabı
- Railway hesabı (ücretsiz)
- Projeniz GitHub'da

## 🔧 Adım Adım Kurulum

### 1. Railway'e Giriş
1. [Railway.app](https://railway.app) adresine gidin
2. "Login" butonuna tıklayın
3. GitHub hesabınızla giriş yapın

### 2. Yeni Proje Oluşturma
1. "New Project" butonuna tıklayın
2. "Deploy from GitHub repo" seçin
3. GitHub repo'nuzu seçin
4. "Deploy Now" butonuna tıklayın

### 3. Servisleri Ekleme
Railway'de 4 ayrı servis oluşturmanız gerekiyor:

#### A. PostgreSQL Database
1. "New Service" → "Database" → "PostgreSQL" seçin
2. Servis adı: `postgres-db`
3. Connection string'i kopyalayın

#### B. Product Service
1. "New Service" → "GitHub Repo" seçin
2. Root Directory: `services/product-service`
3. Build Command: `mvn clean package -DskipTests`
4. Start Command: `java -jar target/product-service-1.0.0.jar`
5. Environment Variables:
   - `SPRING_PROFILES_ACTIVE=prod`
   - `SPRING_DATASOURCE_URL=${{Postgres.DATABASE_URL}}/productdb`
   - `SPRING_DATASOURCE_USERNAME=${{Postgres.USERNAME}}`
   - `SPRING_DATASOURCE_PASSWORD=${{Postgres.PASSWORD}}`

#### C. Order Service
1. "New Service" → "GitHub Repo" seçin
2. Root Directory: `services/order-service`
3. Build Command: `mvn clean package -DskipTests`
4. Start Command: `java -jar target/order-service-1.0.0.jar`
5. Environment Variables:
   - `SPRING_PROFILES_ACTIVE=prod`
   - `SPRING_DATASOURCE_URL=${{Postgres.DATABASE_URL}}/orderdb`
   - `SPRING_DATASOURCE_USERNAME=${{Postgres.USERNAME}}`
   - `SPRING_DATASOURCE_PASSWORD=${{Postgres.PASSWORD}}`

#### D. Gateway Service
1. "New Service" → "GitHub Repo" seçin
2. Root Directory: `gateway`
3. Build Command: `mvn clean package -DskipTests`
4. Start Command: `java -jar target/gateway-1.0.0.jar`
5. Environment Variables:
   - `SPRING_PROFILES_ACTIVE=prod`

#### E. UI Service
1. "New Service" → "GitHub Repo" seçin
2. Root Directory: `ui`
3. Build Command: `mvn clean package -DskipTests`
4. Start Command: `java -jar target/ui-1.0.0.jar`
5. Environment Variables:
   - `SPRING_PROFILES_ACTIVE=prod`

### 4. Servis Bağlantıları
1. Her servise tıklayın
2. "Settings" → "Networking" bölümüne gidin
3. "Generate Domain" butonuna tıklayın
4. Domain'leri not edin

### 5. Environment Variables Güncelleme
Gateway servisinde:
- `PRODUCT_SERVICE_URL=https://your-product-service-domain.railway.app`
- `ORDER_SERVICE_URL=https://your-order-service-domain.railway.app`

UI servisinde:
- `GATEWAY_URL=https://your-gateway-domain.railway.app`

## 🌐 Erişim
- Ana uygulama: UI servisinin domain'i
- API Gateway: Gateway servisinin domain'i
- Product API: `{gateway-domain}/api/products`
- Order API: `{gateway-domain}/api/orders`

## 💰 Maliyet
- Ücretsiz tier: $5/ay kredi
- Bu proje için yaklaşık $3-4/ay
- Aylık 500 saat çalışma süresi

## 🔧 Sorun Giderme
1. **Build Hatası**: Maven dependencies kontrol edin
2. **Database Bağlantı Hatası**: Connection string'i kontrol edin
3. **Servis Bulunamadı**: Environment variables'ları kontrol edin

## 📞 Destek
- Railway Discord: https://discord.gg/railway
- Dokümantasyon: https://docs.railway.app