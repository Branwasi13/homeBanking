const app = Vue.

createApp({
    data() {
        return {
            client: [],
            cards:[],
            debitCard:[],
            creditCard:[],
            cardType:[],
            cardColor:[],
            date:""
        }
    },

    created(){
        axios.get("/api/clients/current")
            .then(response => {
                this.allData(response)
            })
            .catch((error) =>{
                console.log(error);
            });
    },
    methods: {
        allData(response){
                this.client = response.data
                this.cards = this.client.cards
                this.typeCards()
                this.date = new Date().toISOString()
        },
        newDate(creationDate){ 
            return  new Date(creationDate).toLocaleDateString('es-AR', {month: '2-digit', year: '2-digit'})
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
        typeCards(){
            this.cards = this.cards.filter(state => state.stateOfCards == true)
            this.cards.forEach(type =>{
                if(type.cardType == "CREDIT"){
                    this.creditCard.push(type)
                }
                if(type.cardType == "DEBIT"){
                    this.debitCard.push(type)
                }
            })
            
        },
        createCards(){
            location.href = "/web/create-cards.html"
        },
        deleteCards(cardNumber){
            Swal.fire({
                title: 'Are you sure?',
                text: "Are you sure you want to delete this card?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete!'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.patch('/api/clients/current/cards/state',`number=${cardNumber}`)
                    .then(
                        Swal.fire(
                            'succes',
                            'Your card was deleted successfully',
                            'success'
                        ),
                        setTimeout(() => {(location.href = "/web/cards.html")}, 1500)
                    )
                    .catch((error) => {
                        Swal.fire({
                            icon: 'error',
                            showConfirmButton: true,
                            text:error.response.data
                        })
                    });
                  
                }
            })
        }
    },
}).mount('#app');