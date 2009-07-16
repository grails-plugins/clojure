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
}