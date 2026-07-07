# SafeTag - User Service

Ce microservice gère l'inscription, l'authentification et les profils des utilisateurs de la plateforme SafeTag.

*   **Port par défaut :** `8084`
*   **Technologies :** Java / Spring Boot

---

## 🚀 API Référence

### 1. Authentification & Enregistrement

#### Création de compte (avec préfixe)
*   **Méthode :** `POST`
*   **URL :** `http://localhost:8084/api/v1/users/register`
*   **Corps (JSON) :**
```json
{
  "email": "test3@safetag.com",
  "password": "password123"
}
``` 

#### Connexion (Login)

* **Méthode :** POST
* **URL :** `http://localhost:8084/api/v1/auth/login`
* **Corps (JSON) :**
```JSON
{
  "email": "test3@safetag.com",
  "password": "password123"
}
```
* **Réponse :**
```JSON
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0M0BzYWZldGFnLmNvbSIsImV4cCI6MTcxOTk5OTk5OX0.signature"
}
```
### 2. Gestion des Utilisateurs

#### Récupérer un utilisateur par son UUID (Sécurisé)
*   **Méthode :** `GET`
*   **URL :** `http://localhost:8084/api/v1/users/{uuid}`
*   **En-tête requis :** `Authorization: Bearer <votre_token_jwt>`
*   **Exemple :** `http://localhost:8084/users/ac05f1ef-0fcc-49c7-a147-24a60b7c345b`


#### 🛠️ Tests rapides (cURL)
**Inscription :**
```
curl -X POST http://localhost:8084/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{"email": "user@safetag.com", "password": "password123"}'
```
**Connexion :**
```
curl -X POST http://localhost:8084/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "user@safetag.com", "password": "password123"}'
```
**Récupérer un utilisateur avec le Token JWT :**
```bash
curl -X GET http://localhost:8084/users/ac05f1ef-0fcc-49c7-a147-24a60b7c345b \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
