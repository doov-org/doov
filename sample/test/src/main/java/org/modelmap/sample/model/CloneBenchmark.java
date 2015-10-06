/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.modelmap.sample.model;

import static java.util.Arrays.asList;
import static org.modelmap.sample.model.FavoriteWebsite.webSite;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.modelmap.core.FieldModel;
import org.modelmap.core.FieldModels;
import org.modelmap.sample.field.SampleFieldIdInfo;
import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Fork(3)
public class CloneBenchmark {

    private SampleModel source;

    @Setup
    public void init() {
        source = SampleModels.sample();
    }

    @Benchmark
    public SampleModel clone_java_bean() {
        SampleModel clone = new SampleModel();

        User user = new User();
        user.setId(source.getUser().getId());
        user.setFirstName(source.getUser().getFirstName());
        user.setLastName(source.getUser().getLastName());
        user.setBirthDate(source.getUser().getBirthDate());
        clone.setUser(user);

        Account account = new Account();
        account.setId(source.getAccount().getId());
        account.setAcceptEmail(source.getAccount().getAcceptEmail());
        account.setEmail(source.getAccount().getEmail());
        account.setEmailTypes(source.getAccount().getEmailTypes());
        account.setLanguage(source.getAccount().getLanguage());
        account.setLogin(source.getAccount().getLogin());
        account.setPassword(source.getAccount().getPassword());
        account.setPhoneNumber(source.getAccount().getPhoneNumber());
        account.setTimezone(source.getAccount().getTimezone());

        account.setTop3WebSite(new ArrayList<>());
        source.getAccount().getTop3WebSite()
                        .forEach(site -> account.getTop3WebSite().add(webSite(site.getName(), site.getUrl())));
        clone.setAccount(account);
        return clone;
    }

    @Benchmark
    public FieldModel clone_field_model() {
        FieldModel fieldModel = new SampleModelWrapper(source);
        FieldModel clone = new SampleModelWrapper();

        asList(fieldModel.getFieldInfos()).forEach(e -> clone.set(e.id(), fieldModel.get(e.id())));
        return clone;
    }

    @Benchmark
    public FieldModel clone_stream_sequential() {
        return FieldModels.stream(new SampleModelWrapper(source), false)
                        .filter(e -> e.getValue() != null)
                        .collect(FieldModels.toFieldModel(SampleFieldIdInfo.values()));
    }

    @Benchmark
    public FieldModel clone_stream_parallel() {
        return FieldModels.stream(new SampleModelWrapper(source), true)
                        .filter(e -> e.getValue() != null)
                        .collect(FieldModels.toFieldModel(SampleFieldIdInfo.values()));
    }
}
