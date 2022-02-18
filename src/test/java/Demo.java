import com.khd.juanofkingplan.AobingRpcFramework;
import com.khd.juanofkingplan.AobingService;
import com.khd.juanofkingplan.AobingServiceImpl;
import org.junit.Test;

/**
 * @Author kehd
 * @Date 2022-2-17 20:00
 * @Version 1.0
 * @Description
 */
public class Demo
{

    @Test
    public void export() throws Exception {
        //服务提供者只需要暴露出接口
        AobingService service = new AobingServiceImpl();
        AobingRpcFramework.export(service, 2333);
    }

    @Test
    public void refer() throws Exception {
        //服务调用者只需要设置依赖
        AobingService service1 = AobingRpcFramework.refer(AobingService.class, "127.0.0.1", 2333);
        System.out.println(service1.hello("aaaaa"));
    }

    }


