package com.example.data.di

import com.cesar.domain.useCase.chat.IChatUserCase
import com.cesar.domain.useCase.getListMessage.GetListMessageCase
import com.cesar.domain.useCase.getListMessage.IGetListMessageCase
import com.cesar.domain.useCase.getMessage.GetMessageCase
import com.cesar.domain.useCase.getMessage.IGetMessageCase
import com.cesar.domain.useCase.loginGoogle.ILoginGoogleCase
import com.cesar.domain.useCase.loginGoogle.LoginGoogleCase
import com.cesar.domain.useCase.logout.ILogoutCase
import com.cesar.domain.useCase.logout.LogoutCase
import com.cesar.domain.useCase.register.IRegisterUserCase
import com.cesar.domain.useCase.register.RegisterUserCase
import com.cesar.domain.useCase.userList.IUserListCase
import com.cesar.domain.useCase.userList.UserListCase
import com.example.domain.useCase.login.ChatUserCase
import com.example.domain.useCase.login.ILoginUserCase
import com.example.domain.useCase.login.LoginUserCase
import org.koin.dsl.module

val domainModule = module {
    factory <ILoginUserCase>{ LoginUserCase(get())}
    factory <IRegisterUserCase>{ RegisterUserCase(get())}
    factory <IUserListCase>{ UserListCase(get())}
    factory <IChatUserCase>{ ChatUserCase(get())}
    factory <IGetMessageCase>{ GetMessageCase(get())}
    factory <ILogoutCase>{ LogoutCase(get())}
    factory <ILoginGoogleCase>{ LoginGoogleCase(get())}
    factory <IGetListMessageCase>{ GetListMessageCase(get())}
}

