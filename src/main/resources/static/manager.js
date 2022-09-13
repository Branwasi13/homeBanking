const app = Vue.

createApp({
    data() {
        return {
            clients: [],
            loans:[],
            firstName:"",
            lastName:"",
            email:"",
            loanName:"",
            maxAmount:"",
            payments:""
        }
    },

    created(){
        axios.get("/api/clients")
            .then(response => {
                this.clients = response.data;
            })
            .catch((error) =>{
                console.log(error);
            });
        axios.get("/api/loans")
        .then(response => {
            this.loans = response.data
        })
    },
    methods: {
        addClient(){
            axios.post("/rest/clients", {
                firstName: this.firstName,
                lastName: this.lastName,
                email: this.email,
            })
            .then(() => window.location.reload())
            .catch(function (error) {
                console.log(error);
            })
        },
        addLoan(){
            axios.post("/api/admin/loans",`name=${this.loanName}&maxAmount=${this.maxAmount}&payments=${this.payments}`,
            {headers: {'content-type': 'application/x-www-form-urlencoded'}}) 
            .then(() => window.location.reload())
            .catch(function (error) {
                console.log(error);
            })
        },
        logOut(){
            axios.post('/api/logout')
            .then(location.href = "/web/index.html")
        },
    },
}).mount('#app');