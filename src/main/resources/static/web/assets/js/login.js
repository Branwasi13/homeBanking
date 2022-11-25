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
                axios.post('/api/login',"email=" + this.emailLogin + "&pwd=" + this.pwdLogin,{headers : {'Content-Type':'application/x-www-form-urlencoded'}})
                    .then(() =>{
                        if(this.emailLogin.includes("@admin.com")){
                            location.href = "/manager.html"
                        }else{
                            location.href = "/web/accounts.html"
                        }
                    })
                    .catch(() => {
                        if(this.emailLogin == "" && this.pwdLogin == ""){
                            Swal.fire({
                                icon: 'error',
                                text: 'the fields are empty'
                            })
                        }
                        else{
                            Swal.fire({
                                icon: 'error',
                                text: 'the password or email is incorrect'
                            })
                        }
                        
                    });
            },
            register() {
                axios.post('/api/clients',"firstName=" + this.firstName + "&lastName=" + this.lastName + "&email=" + this.emailRegister + "&password=" + this.pwdRegister,{headers : {'Content-Type':'application/x-www-form-urlencoded'}})
                    .then(() =>{
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
                    })
                    .catch((error) => {
                        if(error.response.status == 403){
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                                showConfirmButton: false,
                                timer:2500
                            })
                        }
                       
                    });
            }

        },
    }).mount('#app');


    const sign_in_btn = document.querySelector("#sign-in-btn");
    const sign_up_btn = document.querySelector("#sign-up-btn");
    const container = document.querySelector(".container");

    sign_up_btn.addEventListener("click", () => {
    container.classList.add("sign-up-mode");
    });

    sign_in_btn.addEventListener("click", () => {
    container.classList.remove("sign-up-mode");
    });