import com.rose.common.util.JsonUtil;
import com.rose.conf.Application;
import com.rose.dbopt.repository.TbUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TbUserTest {

	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private TbMqMsgMapper tbMqMsgMapper;
	@Autowired
	private TbUserRepository tbUserRepository;
	@Autowired
	private TbMqMsgPushReleationMapper tbMqMsgPushReleationMapper;
	@Inject
	private MqMsgManageService mqMsgManageService;

	@Test
	public void testSelect() throws Exception {
		TbUser user = tbUserMapper.selectByPrimaryKey(1L);
        System.out.println(user.getUname());
    }

	@Test
	public void testFindById() throws Exception {
		Optional<TbUser> tbUserOptional = tbUserRepository.findById(1L);
		if (tbUserOptional.isPresent()) {
			TbUser user = tbUserOptional.get();
			System.out.println(JsonUtil.objectToJson(user));
		}
	}

	@Test
	public void testListByUname() throws Exception {
		List<TbUser> userList = tbUserRepository.listByUnameAndPwd("111", "222");
		System.out.println(JsonUtil.objectToJson(userList));
	}

    @Test
    public void testMqMsgMapper() throws Exception {
        MqMsgSearchRequest param = new MqMsgSearchRequest();
        param.setStart(10L);
        param.setRows(10);
        List<TbMqMsg> list = tbMqMsgMapper.selectByOption(param);
        System.out.println(1);
    }

	@Test
	public void testMqMsgReleation() {
		//List list = tbMqMsgPushReleationMapper.listByMqMsgId(1L);
		//System.out.println(1);
		TbMqMsg mqMsg = mqMsgManageService.queryDetail(1L);
		System.out.println(1);
	}
}