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
                axios.post('/api/logout')
                .then(location.href = "./index.html")
            },
            pay(){
                axios.post('/api/clients/current/transactions/payments',
                {
                    "id":`${this.card}`,
                    "cardHolder":`${this.cardHolder}`,
                    "number":`${this.cardNumber}`,
                    "amount":`${this.amount}`,
                    "cvv":`${this.cvv}`,
                    "fromDate":`${this.date}`,
                    "description":`${this.description}`
                })
                .then(() =>
                    
                        Swal.fire(
                            'succes',
                            'your payment has been send',
                            'success',
                            setTimeout(() => { location.href = "/web/accounts.html"}, 1000)
                        )
                    
                )
                .catch(error =>
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: error.response.data,
                    }),
                )
            }
            

        },
    }).mount('#app');