# 🧑‍💻 Simple HRIS

Simple HRIS adalah sebuah sistem informasi sumber daya manusia yang dirancang untuk membantu perusahaan dalam mengelola data dan proses kepegawaian secara terpusat dan efisien.

### Mengapa sistem ini dibutuhkan?
Pengelolaan data karyawan secara manual atau terpisah sering kali menimbulkan berbagai permasalahan, seperti data yang tidak konsisten, sulit diakses, serta rawan kesalahan. 

### Bagaimana sistem bekerja?
1. HR Admin dapat mengelola data karyawan yang meliputi data personal karyawan dan data karir karyawan.
2. Paymaster dapat mengelola data gaji karyawan.

### Apa manfaat dari sistem ini?
Dengan Simple HRIS, perusahaan dapat mengelola data karyawan secara lebih efektif, mempercepat proses administrasi HR, serta meningkatkan akurasi dan transparansi data kepegawaian.

## Tech Stack
- Java Spring Boot
- MySQL
- Spring Security + JWT
- JPA Hibernate
- Redis Cache

## 📊 ERD
<img width="1001" height="641" alt="simple-hris drawio" src="https://github.com/user-attachments/assets/6d9381e8-e5a3-44ef-b8ce-919ce3796fdd" />

## ⚙️ Roles & Authorization

1. EMPLOYEE : Melihat data kepegawainnya

2. HRADMIN : Mengelola data user, data personal karyawan, mengelola master posisi dan data karir karyawan.

3. PAYMASTER : Update gaji karyawan

4. SUPERADMIN : Full Access


## 📌API Endpoints

- POST `/auth/login` : Untuk login

  Request:
  ```bash
  {
    "username": "superadmin",
    "password": "Password123!"
  }
  ```

  Response:
  ```bash
  {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlcmFkbWluIiwiaWF0IjoxNzcwNzMxNjYzLCJleHAiOjE3NzA3MzM0NjN9.4ANjoYryXnG-ai8C601efIhTtbTeN_mxk9XQsnIERRE",
    "type": "Bearer"
  }
  ```

- GET  `/employees` : Untuk melihat data semua karyawan (ROLE: SUPERADMIN, HRADMIN)

  Response:
  ```bash
  {
        "dateOfBirth": "2000-01-01",
        "firstName": "Ayu",
        "hireDate": "2025-01-01",
        "lastName": "Ningsih",
        "maritalStatus": "MARRIED",
        "middleName": "Sukma",
        "placeOfBirth": "Batam",
        "religion": "ISLAM",
        "role": "HRADMIN",
        "username": "ayu.ningsih"
    },
    {
        "dateOfBirth": "2000-01-01",
        "firstName": "sinta",
        "hireDate": "2025-01-01",
        "lastName": "",
        "maritalStatus": "MARRIED",
        "middleName": "",
        "placeOfBirth": "Batam",
        "religion": "ISLAM",
        "role": "PAYMASTER",
        "username": "sinta"
    },
    {
        "dateOfBirth": "2002-01-01",
        "firstName": "budi",
        "hireDate": "2022-05-01",
        "lastName": "kurniawan",
        "maritalStatus": "MARRIED",
        "middleName": "aja",
        "placeOfBirth": "Singapore",
        "religion": "BUDDHA",
        "role": "EMPLOYEE",
        "username": "budi.kurniawan"
    }
  ```


- POST  `/employees` : Untuk menambahkan karyawan (ROLE: SUPERADMIN, HRADMIN)

  Request:
  ```bash
  {
    "username": "john",
    "password": "12345",
    "role": "EMPLOYEE",
    "firstName": "john",
    "middleName": "",
    "lastName": "kurniawan",
    "hireDate": "2022-05-01",
    "placeOfBirth": "Singapore",
    "dateOfBirth": "2002-01",
    "religion": "BUDDHA",
    "maritalStatus": "MARRIED",
    "positionId": "5",
    "endDate": "",
    "employmentStatus": "PERMANENT",
    "jobDescription": ""
  }
  ```

- PATCH  `/employees/personal/8` : Untuk update data personal karyawan (ROLE: SUPERADMIN, HRADMIN)

  Request:
  ```bash
  {
    "firstName": "abdul",
    "lastName": "",
    "middleName": "setiawan",
    "placeOfBirth": "Batam",
    "dateOfBirth": "2005-10-10"
  }
  ```
  
- DELETE  `/employees/8` : Untuk menghapus data karyawan (ROLE: SUPERADMIN, HRADMIN)

- PATCH  `/employees/salary/8` : Untuk update data gaji karyawan (ROLE: SUPERADMIN, PAYMASTER)

  Request:
  ```bash
  {
    "salary": "12000000"
  }
  ```

- POST  `/positions` : Untuk menambahkan posisi  (ROLE: SUPERADMIN, HRADMIN)

  Request:
  ```bash
  {
    "name": "Front End Engineer"
  }
  ```

- PATCH  `/positions/8` : Untuk update nama posisi  (ROLE: SUPERADMIN, HRADMIN)

  Request:
  ```bash
  {
    "name": "Front End Engineer Senior"
  }
  ```

- DELETE  `/positions/8` : Untuk menghapus posisi  (ROLE: SUPERADMIN, HRADMIN)

- GET `/positions/8` : Untuk mendapatkan data posisi (ROLE: SUPERADMIN, HRADMIN)

  Response:
  ```bash
  {
    "name": "Front End Engineer Senior"
  }
  ```  

  

## How to Run

- Clone project
```bash
git clone git@github.com:perdlywan/Java-Spring-Boot-Simple-HRIS.git
```

- Setup config application.properties

- Run project 
```bash
mvn spring-boot:run
```

## Deployment 🚀🚀

Base URL
```bash
http://203.194.115.210:9002
```



Deployment steps:

1️⃣ Prerequisites
- VPS aktif & bisa SSH
- Docker & Docker Compose ter-install di VPS
- Akun DockerHub
- Repository GitHub

2️⃣ Setup Repository
- Push source code ke GitHub
- Tambahkan GitHub Secrets:
  - VPS_HOST
  - VPS_USER
  - VPS_SSH_KEY
  - DOCKERHUB_USERNAME
  - DOCKERHUB_TOKEN
 
3️⃣ Konfigurasi Docker
- Buat Dockerfile untuk build Spring Boot
- Buat docker-compose.yml
  - expose port aplikasi
  - gunakan env_file: .env
  - aktifkan healthcheck
 
4️⃣ Setup Environment di VPS

Login ke vps
```bash
ssh user@203.194.115.210
```

Masuk ke folder aplikasi dan buat file .env
```bash
APP_PORT=9002
DB_HOST=mysql
DB_PORT=3306
DB_NAME=your_db
DB_USER=root
DB_PASSWORD=your_password
```

5️⃣ CI/CD dengan GitHub Actions
- Workflow otomatis berjalan saat push ke branch main
- Proses:
  - Build aplikasi
  - Build & push Docker image ke DockerHub
  - SSH ke VPS
  - Pull image terbaru
  - Restart container menggunakan docker-compose

6️⃣ Jalankan Aplikasi
Cek container:
```bash
docker ps
```

Cek log:
```bash
docker logs spring-deploy-student2-app
```

7️⃣ Akses Aplikasi
```bash
http://203.194.115.210:9002:9002
```

<img width="1369" height="659" alt="image" src="https://github.com/user-attachments/assets/5c963be7-e473-47d4-92db-b78301ba9838" />
<img width="1355" height="919" alt="image" src="https://github.com/user-attachments/assets/e12ed9a9-47cf-47ed-9f2f-d5b12c75c551" />
<img width="1351" height="640" alt="image" src="https://github.com/user-attachments/assets/cf0cc3a0-c6ac-4f43-94ac-f2b1f305b037" />





