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
            selectedLoanType : '',
            name : '',
            amount : '',
            interest : '',
            payments : [],
            targetAccount : '',
            error : "",
            selectedAccount: '',
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
            axios('/api/loans/availableLoans')
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
                this.selectedAccount = this.accounts.find(item => item.id == account)
                console.log(response)
            })
            .catch(error => console.log(error))
        },
        parsePayments(value) {
            return JSON.parse(value).map(Number);
        },
        createLoan() {
            this.error = "";

            const parsedPayments = this.parsePayments(this.payments);

            if (!this.interest || !this.name || !this.amount || !parsedPayments) {
                this.error = "Please fill in all required fields.";
                return;
            }
        
            const confirmationMessage = `Are you sure you want to create the ${this.name} of ${this.amount.toLocaleString("en-US", { style: "currency", currency: "USD" })} with ${parsedPayments} payments, interest rate of ${(this.interest)*100}%?`;
        
            Swal.fire({
                title: "Create loan?",
                text: confirmationMessage,
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Proceed"
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/loans/create', {
                        "name": this.name,
                        "maxAmount": this.amount,
                        "payments": parsedPayments,
                        "interest": this.interest
                    })
                        .then(response => {
                            Swal.fire({
                                title: "Success",
                                text: "New loan created successfully",
                                icon: "success"
                            });
        
                            setTimeout(() => {
                                window.location.href = "../admin/managerOverview.html";
                            }, 2000);
                            this.clearData();
                            console.log(response);
                        })
                        .catch(error => {
                            this.error = error.response.data;
                            console.log(error);
                        });
                }
            });
        },
        clearErrors() {
            this.error = "";
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
    },

    computed: {
        formattedMaxAmount() {
            if (this.selectedLoanType) {
                const maxAmount = this.selectedLoanType.maxAmount || 0;
    
                return maxAmount.toLocaleString("en-US", {
                    style: "currency",
                    currency: "USD"
                });
            }
    
            return null;
        },
        estimatedTotal() {
            if (this.selectedLoanType) {
                return (this.amount * (1 + this.selectedLoanType.interest)).toLocaleString("en-US", {
                    style: "currency",
                    currency: "USD"
                });
            }
    
            return null;
        }
    }
}).mount('#app')