package com.leikooo.mallchat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.chat.mapper.ContactMapper;
import com.leikooo.mallchat.common.user.domain.entity.Contact;
import com.leikooo.mallchat.common.user.service.IContactService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会话列表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-03-02
 */
@Service
public class ContactDao extends ServiceImpl<ContactMapper, Contact> {

}
