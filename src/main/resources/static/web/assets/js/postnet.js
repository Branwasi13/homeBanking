const app = Vue.
    createApp({
        data() {
            return {
                client:[],
                cards:[],
                cardHolder:"",
                cardNumber:"",
                card:"",
                amount:"",
                date:"",
                cvv:"",
                description:""
            }
        },
        created() {
            axios.get("/api/cards")
            .then(response => {
                this.allData(response)
            })
            .catch((error) =>{
                console.log(error);
            })
            axios.get("/api/clients/current")
            .then(response => {
                this.client = response.data
            })
        },
        methods: {
            allData(response){
                this.cards = response.data
                this.cards = this.cards.filter(state => state.stateOfCards == true)
            },
            logOut(){
                Swal.fire({
                    title: 'Are you sure?',
                    text: "Are you sure you want to close session?",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes, Log out!'
                  }).then((result) => {
                    if (result.isConfirmed) {
                      Swal.fire(
                        'succes',
                        'The session has been successfully closed',
                        'success'
                      )
                      setTimeout(() => { 
                            axios.post('/api/logout')
                            .then(location.href = "./index.html")
                        }, 1500)
                      
                    }
                  })
                
            },
            pay(){
                axios.post('/api/clients/current/transactions/payments',
                {
                    "cardHolder":`${this.cardHolder}`,
                    "number":`${this.cardNumber}`,
                    "amount":`${this.amount}`,
                    "cvv":`${this.cvv}`,
                    "thruDate":`${this.date}`,
                    "description":`${this.description}`
                })
                .then(() =>
                    Swal.fire(
                        'succes',
                        'your payment has been send',
                        'success',
                        setTimeout(() => { location.href = "/web/accounts.html"}, 2500)
                    )
                    
                )
                .catch(error =>
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: error.response.data,
                    })
                )
            }
            

        },
    }).mount('#app');