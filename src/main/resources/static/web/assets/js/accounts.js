const app = Vue.

createApp({
    data() {
        return {
            client: [],
            accounts:[],
            loans:[],
            totalAccountsBalance:0,
            totalLoansBalance:0,
            totalBalance:0,
            accountsBalancePercentage:0,
            paymentsBalancePercentage:0,
            accountType:""
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
                this.accounts = response.data.accounts
                this.accounts = this.accounts.filter(state => state.accountState == true)
                this.loans = response.data.clientLoans
                this.balanceAccounts()
                this.balancePercentages()
                this.grafic()
        },
        newDate(creationDate){
           return creationDate = new Date(creationDate).toLocaleDateString()
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
        grafic(){
            let ctx = document.getElementById("miChart").getContext("2d");
            let myChart = new Chart(ctx,{
                type:"doughnut",
                data:{
                    labels:['Ac balance' + ' ' +this.accountsBalancePercentage + '%','Loans' + ' ' + this.paymentsBalancePercentage + '%'],
                    datasets:[{
                        label:'Num datos',
                        data:[this.totalAccountsBalance,this.totalLoansBalance],
                        backgroundColor:[
                            'rgb(66, 134, 244)',
                            'rgb(74, 135, 72)'
                        ]
                    }]
                }
            })
        },
        balanceAccounts(){
            this.accounts.forEach(account => {
                this.totalAccountsBalance += account.balance
            });
            this.loans.forEach(loan =>{
                this.totalLoansBalance += loan.amount
            })
            
        },
        balancePercentages(){
            this.totalBalance = this.totalAccountsBalance + this.totalLoansBalance
            this.accountsBalancePercentage = (this.totalAccountsBalance * 100) / this.totalBalance
            this.paymentsBalancePercentage = (this.totalLoansBalance * 100) / this.totalBalance
            this.accountsBalancePercentage = Math.round(this.accountsBalancePercentage)
            this.paymentsBalancePercentage = Math.round(this.paymentsBalancePercentage)
        },
        newAccount(){
            axios.post("/api/clients/current/accounts",`accountType=${this.accountType}`,
                {headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(
                    Swal.fire(
                        'Good job!',
                        'You account has been created!',
                        'success',
                        setTimeout(() => { location.href = "/web/accounts.html" }, 1500)
                    )
                )
                .catch(error =>
                    Swal.fire({
                        icon: 'error',
                        title: 'You cannot create more accounts (MAX-3)',
                    }),
                    setTimeout(() => { location.href = "/web/accounts.html" }, 1000)
                )
        },
        applyLoan(){
            location.href = "/web/loan-application.html"
        },
        deleteAccount(id){
            axios.patch('/api/clients/current/accounts/delete',`accountId=${id}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(
                Swal.fire(
                    'succes',
                    'This account has been successfully deleted',
                    'success'
                ),
                setTimeout(() => {(location.href = "/web/accounts.html")}, 2000)
            )
            .catch((error) => {
                Swal.fire({
                    icon: 'error',
                    timer:1500,
                    showConfirmButton: false,
                    text:error.response.data
                })
            });
                  
                 
        }
    },
}).mount('#app');