const app = Vue.

createApp({
    data() {
        return {
            client:[],
            cards:[],
            debitCard:[],
            creditCard:[],
            cardType:[],
            cardColor:[],
            cardType:"",
            cardColor:"",
            button:""
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
        },
        newDate(creationDate){ 
            return  new Date(creationDate).toLocaleDateString('es-AR', {month: '2-digit', year: '2-digit'})
        },
        logOut(){
            axios.post('/api/logout')
            .then(location.href = "./index.html")
        },
        typeCards(){
            this.cards.forEach(type =>{
                if(type.cardType == "CREDIT"){
                    this.creditCard.push(type)
                }
                if(type.cardType == "DEBIT"){
                    this.debitCard.push(type)
                }
            })
        },
        newCard(success){
            axios.post('/api/clients/current/cards', `cardColor=${this.cardColor}&cardType=${this.cardType}`,
                    {headers: {'content-type': 'application/x-www-form-urlencoded'}})
                    .then(() => {
                        if(success){
                            Swal.fire({
                                position: 'top-end',
                                icon: 'success',
                                title: 'Your card was created successfully',
                                showConfirmButton: false,
                                timer:1500
                            }),
                           setTimeout(() => { location.href = "/web/cards.html"}, 1500)
                        }
                    })
                    .catch((error) => {
                        Swal.fire({
                            icon: 'error',
                            timer:2000,
                            showConfirmButton: false,
                            text:error.response.data
                        }),
                        setTimeout(() => { location.href = "/web/create-cards.html"}, 2000)
                    });
        },
        removeDisableSection(id1,id2){
            let disableSectionColumn1 = document.getElementById(id2)
            let button = document.getElementById(id1)
            disableSectionColumn1.classList.remove("disableSection")
            if(button){
                button.classList.add("d-none")
            }
        },
        addDisableSection(id1,id2,id3){
            let disableSectionColumn1 = document.getElementById(id2)
            let button1 = document.getElementById(id1)
            let button2 = document.getElementById(id3)
            disableSectionColumn1.classList.add("disableSection")
            if(button1){
                button1.classList.remove("d-none")
            }
            if(button2){
                button2.classList.remove("d-none")
            }
        },
        valueButtonType(type){
            this.cardType = type
        },
        valueButtonColor(color){
            this.cardColor = color
        },
        changeColor(color) {
            let frontCardColor = document.getElementById("frontCard")
            let backCardColor = document.getElementById("backCard")
            switch (color) {
                case "TITANIUM":
                    if (frontCardColor.classList.contains('card_gold') && backCardColor.classList.contains('card_gold')) {
                        frontCardColor.classList.remove('card_gold')
                        backCardColor.classList.remove('card_gold')
                    }
                    if (frontCardColor.classList.contains('card_silver') && backCardColor.classList.contains('card_silver')) {
                        frontCardColor.classList.remove('card_silver')
                        backCardColor.classList.remove('card_silver')
                    }
                    frontCardColor.classList.add('card_titanium')
                    backCardColor.classList.add('card_titanium')
                    break;
                case "SILVER":
                    if (frontCardColor.classList.contains('card_gold') && backCardColor.classList.contains('card_gold')) {
                        frontCardColor.classList.remove('card_gold')
                        backCardColor.classList.remove('card_gold')
                    }
                    if (frontCardColor.classList.contains('card_titanium') && backCardColor.classList.contains('card_titanium')) {
                        frontCardColor.classList.remove('card_titanium')
                        backCardColor.classList.remove('card_titanium')
                    }
                    frontCardColor.classList.add('card_silver')
                    backCardColor.classList.add('card_silver')
                    break;
                case "GOLD":
                    if (frontCardColor.classList.contains('card_silver') && backCardColor.classList.contains('card_silver') ) {
                        frontCardColor.classList.remove('card_silver')
                        backCardColor.classList.remove('card_silver')
                    }
                    if (frontCardColor.classList.contains('card_titanium') && backCardColor.classList.contains('card_titanium')) {
                        frontCardColor.classList.remove('card_titanium')
                        backCardColor.classList.remove('card_titanium')
                    }
                    frontCardColor.classList.add('card_gold')
                    backCardColor.classList.add('card_gold')
                    break;
                default:
                    break;
            }
        }
    },
}).mount('#app');