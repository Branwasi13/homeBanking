const app = Vue.
    createApp({
        data() {
            return {
                emailLogin: "",
                pwdLogin: "",
                emailRegister: "",
                pwdRegister: "",
                firstName: "",
                lastName: "",
            }
        },
        created() {
            
        },
        methods: {
            
            login() {
                axios.post("/api/login", `email=${this.emailLogin}&pwd=${this.pwdLogin}`,
                    { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then(() =>{
                        if(this.emailLogin.includes("@admin.com")){
                            location.href = "/manager.html"
                        }else{
                            location.href = "/web/accounts.html"
                        }
                    })
                    .catch((error) => {
                        Swal.fire({
                            icon: 'error',
                            text: 'the password or email is incorrect'
                        })
                    });
            },
            register() {
                axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.pwdRegister}`,
                    { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then(() =>
                        axios.post("/api/login", `email=${this.emailRegister}&pwd=${this.pwdRegister}`,
                        {headers:{'content-type': 'application/x-www-form-urlencoded'}}
                        ),
                        Swal.fire({
                            position: 'top-end',
                            icon: 'success',
                            title: 'Your account was created successfully',
                            showConfirmButton: false,
                            timer:1500
                        }),
                        setTimeout(() => { location.href = "/web/accounts.html"}, 1500)
                    )
                    .catch((error) => {
                        Swal.fire({
                            icon: 'error',
                            text: error.response.data,
                            showConfirmButton: false,
                            timer:1500
                        })
                        setTimeout(() => { location.href = "/web/login.html"}, 1500)
                    });
            }

        },
    }).mount('#app');