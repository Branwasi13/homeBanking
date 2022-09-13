const app = Vue.

createApp({
    data() {
        return {
            client:[],
            accounts:[],
            payments:[],
            loans:[],
            payment:"",
            loanId:"",
            loanPays:1,
            amount:"",
            accountDestiny:"",
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
        axios.get("/api/loans")
            .then(response => {
                this.getLoans(response)
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
        getLoans(response){
                this.loans = response.data
        },
        newDate(creationDate){ 
            return  new Date(creationDate).toLocaleDateString('es-AR', {month: '2-digit', year: '2-digit'})
        },
        logOut(){
            axios.post('/api/logout')
            .then(location.href = "./index.html")
        },
        loanPetition(){
            Swal.fire({
                title: 'Are you sure?',
                text: "Are you sure to apply for this loan?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, apply loan!'
            }).then((result) => {
                if (result.isConfirmed) {
                        axios.post('/api/loans',
                    {
                        "id":`${this.loanId}`,
                        "amount":`${this.amount}`,
                        "payments":`${this.loanPays}`,
                        "destinyAccount":`${this.accountDestiny}`
                    })
                    .then(() =>
                        Swal.fire({
                            icon: 'success',
                            title: 'your loan has been credited',
                            showConfirmButton: false,
                            timer:1500
                        })
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
            
        },
    },
    computed:{
        interest(){
            switch (this.loanId){
                case 2:
                    switch (this.loanPays){
                        case 6: 
                            return Math.round(this.amount * 1.20);
                        case 12: 
                            return Math.round(this.amount * 1.30);
                        case 24:
                            return Math.round(this.amount * 1.40);

                    }
                    break;
                case 1:
                    switch (this.loanPays){
                        case 12: 
                            return Math.round(this.amount * 1.30);
                        case 24: 
                            return Math.round(this.amount * 1.40);
                        case 36: 
                            return Math.round(this.amount * 1.45);
                        case 48: 
                            return Math.round(this.amount * 1.50);
                        case 60: 
                            return Math.round(this.amount * 1.55);
                    }
                    break;
                case 3:
                    switch (this.loanPays) {                       
                        case 6:
                            return Math.round(this.amount * 1.20);
                        case 12:
                            return Math.round(this.amount * 1.30);
                        case 24:
                            return Math.round(this.amount * 1.40);
                        case 36:
                            return Math.round(this.amount * 1.45);
                    }
                    break;
                default:
                    break;
    
            } 
        }
    }
}).mount('#app');