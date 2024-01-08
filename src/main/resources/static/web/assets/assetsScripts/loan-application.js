const {createApp} = Vue

let app = createApp({
    data(){
        return {
            data: [],
            client: [],
            firstName : "",
            lastName : "",
            email : "",
            accounts : [],
            availableLoans : [],
            id: 1,
            loanId : '',
            amount : '',
            payments : '',
            targetAccount : '',
            error : ""
        }
    },
    created(){
        this.loadLoan()
        this.loadData()
        
    },

    methods: {
        loadData(){
            axios('/api/clients/current')
            .then(response =>{ 
                this.data = response,
                this.client = response.data,
                this.accounts = response.data.accounts
                console.log(this.accounts)
            })
            .catch(error => console.log(error))
        },
        loadLoan(){
            axios('/api/loans')
            .then(response =>{
                this.data = response,
                this.availableLoans = response.data,
                console.log(this.availableLoans)
            })
            .catch(error => console.log(error))
        },
        getBalance(account){
            axios('/api/clients/current')
            .then(response => {
                selectedAccount = this.accounts.find(item => item.id == account)
                console.log(response)
            })
            .catch(error => console.log(error))
        },
        createLoan(){
            axios.post('/api/loans', {
                "id" : this.loanId,
                "amount" : this.amount,
                "payments" : this.payments,
                "targetAccount" : this.targetAccount
            })
            .then(response => {
                Swal.fire({
                  title: "Complete transaction?",
                  text: "You won't be able to revert this!",
                  icon: "warning",
                  showCancelButton: true,
                  confirmButtonColor: "#3085d6",
                  cancelButtonColor: "#d33",
                  confirmButtonText: "Proceed"
                }).then((result) => {
                  if (result.isConfirmed) {
                    Swal.fire({
                      title: "Success",
                      text: "Transaction Completed",
                      icon: "success"
                    });
                  } 
                }, this.clearData()
                )
                console.log(response)
            })
            .catch(error => {
                this.error = error.response.data
                console.log(error)
            })
        },
        logOut(){
            axios('/api/logout')
            .then(
                window.location.href = "../index.html"
            )
        },
        clearData(){
            this.amount = '',
            this.description = '',
            this.originAccount = '',
            this.targetAccount = '',
            this.error = ''
        }
    }
}).mount('#app')