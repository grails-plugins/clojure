class DemoController {
    
    def simple = {
        render clj.simple()
    }
    
    def one = {
        render clj['one'].doit()
    }
    
    def two = {
        render clj['two'].doit()
    }
    
    def fibo = {
        def cnt = params.cnt?.toInteger()
        def numbers
        if(cnt) {
            numbers = clj.fibo(cnt)
        } else {
            numbers = []
        }
        render view: 'fibo', model: [numbers: numbers]
    }
}
