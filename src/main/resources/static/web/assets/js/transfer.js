const app = Vue.

createApp({
    data() {
        return {
            client:[],
            accounts:[],
            tranferType:"own account",
            amount:"",
            description:"",
            originAccount:[],
            destinyAccount:[],
            
        }
    },

    created(){
        axios.get("/api/clients/current")
            .then(response => {
                this.allData(response)
            })
            .catch((error) =>{
                console.log(error)
            });
    },
    methods: {
        allData(response){
                this.client = response.data 
                this.accounts = this.client.accounts
        },
        logOut(){
            axios.post('/api/logout')
            .then(location.href = "./index.html")
        },
        newTranfer(){
                axios.post('/api/clients/current/accounts/transactions', `amount=${this.amount}&description=${this.description}&originAccount=${this.originAccount}&destinyAccount=${this.destinyAccount}`,
                {headers: {'content-type': 'application/x-www-form-urlencoded'}})
                .then(
                    Swal.fire(
                        'Good job!',
                        'You clicked the button!',
                        'success',
                        setTimeout(() => { location.href = "/web/transfers.html"}, 1000)
                    )
                )
                .catch(error =>
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: error.response.data,
                    }),
                    setTimeout(() => { location.href = "/web/transfers.html"}, 1500)
                )
        },
        
    },
}).mount('#app');