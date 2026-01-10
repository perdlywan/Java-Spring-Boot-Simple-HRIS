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
- GET  `/employees` : Untuk melihat data semua karyawan

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


- POST  `/employees` : Untuk menambahkan karyawan

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

- PATCH  `/employees/personal/8` : Untuk update data personal karyawan

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
  
- DELETE  `/employees/8` : Untuk menghapus data karyawan

- PATCH  `/employees/salary/8` : Untuk update data gaji karyawan

  Request:
  ```bash
  {
    "salary": "12000000"
  }
  ```



## How to Run
```bash
mvn spring-boot:run

