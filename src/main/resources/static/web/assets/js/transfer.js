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